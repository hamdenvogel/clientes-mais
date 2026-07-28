package io.github.hvogel.clientes.rest;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.response.jwt.JwtResponse;
import io.github.hvogel.clientes.response.jwt.MessageResponse;
import io.github.hvogel.clientes.rest.dto.CredenciaisDTO;
import io.github.hvogel.clientes.rest.dto.CompleteRegistrationDTO;
import io.github.hvogel.clientes.rest.dto.RegistrationTokenValidationDTO;
import io.github.hvogel.clientes.rest.dto.ResendConfirmationDTO;
import io.github.hvogel.clientes.rest.dto.SignupDTO;
import io.github.hvogel.clientes.rest.dto.SignupInitDTO;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.service.RegistrationFlowService;
import io.github.hvogel.clientes.service.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final RegistrationFlowService registrationFlowService;

  @PostMapping("/signin")
  public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody CredenciaisDTO credenciaisDTO) {
    final Authentication authentication;
    try {
      authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(credenciaisDTO.getLogin(), credenciaisDTO.getSenha()));
    } catch (DisabledException ex) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
          "Seu cadastro ainda nao foi confirmado. Verifique seu e-mail para ativar a conta.");
    } catch (BadCredentialsException ex) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario e/ou senha invalidos.");
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    if (!userDetails.isEnabled()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
          "Seu cadastro ainda nao foi confirmado. Verifique seu e-mail para ativar a conta.");
    }

    String jwt = jwtUtils.gerarToken(authentication);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .toList();

    return ResponseEntity.ok(new JwtResponse(jwt,
        userDetails.getId(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupDTO signUpRequest) {
    throw new ResponseStatusException(HttpStatus.GONE,
        "Este endpoint foi descontinuado. Utilize /api/auth/registration/signup-init para iniciar o cadastro com confirmacao por e-mail.");
  }

  @PostMapping("/registration/signup-init")
  public ResponseEntity<MessageResponse> initiateSignup(@Valid @RequestBody SignupInitDTO signupInitDTO) {
    registrationFlowService.initiateSignup(signupInitDTO);
    return ResponseEntity.ok(new MessageResponse(
        "Cadastro iniciado com sucesso. Enviamos um e-mail de confirmacao com orientacoes para concluir sua conta."));
  }

  @GetMapping("/registration/validate-token")
  public ResponseEntity<RegistrationTokenValidationDTO> validateToken(@RequestParam String token) {
    return ResponseEntity.ok(registrationFlowService.validateToken(token));
  }

  @PostMapping("/registration/complete")
  public ResponseEntity<MessageResponse> completeRegistration(@Valid @RequestBody CompleteRegistrationDTO request) {
    registrationFlowService.completeRegistration(request.getToken(), request.getPassword(), request.getConfirmPassword());
    return ResponseEntity.ok(new MessageResponse(
        "Senha definida com sucesso. Sua conta foi ativada e voce ja pode fazer login."));
  }

  @PostMapping("/registration/resend-confirmation")
  public ResponseEntity<MessageResponse> resendConfirmation(@Valid @RequestBody ResendConfirmationDTO request) {
    registrationFlowService.resendConfirmation(request.getEmail());
    return ResponseEntity.ok(new MessageResponse(
        "Se o e-mail informado estiver pendente de confirmacao, um novo link foi enviado."));
  }
}
