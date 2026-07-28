package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.github.hvogel.clientes.rest.dto.GoogleRecaptchaDTO;
import io.github.hvogel.clientes.service.GoogleService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(GoogleController.class)
@AutoConfigureMockMvc(addFilters = false)
class GoogleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoogleService googleService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testValidarToken() throws Exception {
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();
        dto.setSuccess(true);
        when(googleService.validarToken(anyString())).thenReturn(dto);

        mockMvc.perform(post("/api/google/some-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoverTentativas() throws Exception {
        doNothing().when(googleService).zerarTentativasMalSucedidas();

        mockMvc.perform(get("/api/google"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testInformarValidacao() throws Exception {
        mockMvc.perform(get("/api/google/informa-validacao"))
                .andExpect(status().isOk());
    }

    @Test
    void testValidarToken_Exception() throws Exception {
        when(googleService.validarToken(anyString()))
                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Error"));

        mockMvc.perform(post("/api/google/some-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRemoverTentativas_Exception() throws Exception {
        org.mockito.Mockito.doThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Error"))
                .when(googleService).zerarTentativasMalSucedidas();

        mockMvc.perform(get("/api/google"))
                .andExpect(status().isBadRequest());
    }
}
