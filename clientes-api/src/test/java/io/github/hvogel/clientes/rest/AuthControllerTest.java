package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.github.hvogel.clientes.rest.dto.CompleteRegistrationDTO;
import io.github.hvogel.clientes.rest.dto.CredenciaisDTO;
import io.github.hvogel.clientes.rest.dto.RegistrationTokenValidationDTO;
import io.github.hvogel.clientes.rest.dto.ResendConfirmationDTO;
import io.github.hvogel.clientes.rest.dto.SignupDTO;
import io.github.hvogel.clientes.rest.dto.SignupInitDTO;
import io.github.hvogel.clientes.service.RegistrationFlowService;
import io.github.hvogel.clientes.service.impl.UserDetailsImpl;
import io.github.hvogel.clientes.test.base.BaseControllerTest;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends BaseControllerTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private RegistrationFlowService registrationFlowService;

    @Test
    void testAuthenticateUser() throws Exception {
        CredenciaisDTO credenciais = new CredenciaisDTO();
        credenciais.setLogin("user");
        credenciais.setSenha("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user", "user@test.com", "password", Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.gerarToken(authentication)).thenReturn("jwt-token");

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credenciais)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    void testAuthenticateUser_Failure() throws Exception {
        CredenciaisDTO credenciais = new CredenciaisDTO();
        credenciais.setLogin("user");
        credenciais.setSenha("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credenciais)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errors[0]").value("Usuario e/ou senha invalidos."));
    }

    @Test
    void testAuthenticateUser_DisabledAccount() throws Exception {
        CredenciaisDTO credenciais = new CredenciaisDTO();
        credenciais.setLogin("pendente");
        credenciais.setSenha("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "pendente", "pendente@test.com", "password",
                Collections.emptyList(), false);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credenciais)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errors[0]").value(
                    "Seu cadastro ainda nao foi confirmado. Verifique seu e-mail para ativar a conta."));
    }

    @Test
    void testLegacySignupEndpointIsGone() throws Exception {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUsername("newuser");
        signupDTO.setEmail("newuser@test.com");
        signupDTO.setPassword("Senha@123");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDTO)))
            .andExpect(status().isGone())
            .andExpect(jsonPath("$.errors[0]").value(
                    "Este endpoint foi descontinuado. Utilize /api/auth/registration/signup-init para iniciar o cadastro com confirmacao por e-mail."));
    }

    @Test
    void testInitiateSignup() throws Exception {
        SignupInitDTO request = new SignupInitDTO();
        request.setUsername("newuser");
        request.setEmail("newuser@test.com");

        doNothing().when(registrationFlowService).initiateSignup(any(SignupInitDTO.class));

        mockMvc.perform(post("/api/auth/registration/signup-init")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testValidateToken() throws Exception {
        when(registrationFlowService.validateToken("abc-token"))
            .thenReturn(RegistrationTokenValidationDTO.builder().valid(true).message("ok").build());

        mockMvc.perform(get("/api/auth/registration/validate-token").param("token", "abc-token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    void testCompleteRegistration() throws Exception {
        CompleteRegistrationDTO request = new CompleteRegistrationDTO();
        request.setToken("abc-token");
        request.setPassword("Senha@123");
        request.setConfirmPassword("Senha@123");

        doNothing().when(registrationFlowService).completeRegistration("abc-token", "Senha@123", "Senha@123");

        mockMvc.perform(post("/api/auth/registration/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testResendConfirmation() throws Exception {
        ResendConfirmationDTO request = new ResendConfirmationDTO();
        request.setEmail("user@test.com");

        doNothing().when(registrationFlowService).resendConfirmation("user@test.com");

        mockMvc.perform(post("/api/auth/registration/resend-confirmation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").exists());
    }
}
