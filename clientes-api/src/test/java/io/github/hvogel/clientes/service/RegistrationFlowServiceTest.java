package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.enums.RegistrationTokenPurpose;
import io.github.hvogel.clientes.model.entity.Perfil;
import io.github.hvogel.clientes.model.entity.RegistrationToken;
import io.github.hvogel.clientes.model.entity.Usuario;
import io.github.hvogel.clientes.model.repository.PerfilRepository;
import io.github.hvogel.clientes.model.repository.RegistrationTokenRepository;
import io.github.hvogel.clientes.model.repository.UsuarioRepository;
import io.github.hvogel.clientes.rest.dto.SignupInitDTO;

@ExtendWith(MockitoExtension.class)
class RegistrationFlowServiceTest {

  @Mock
  private UsuarioRepository usuarioRepository;

  @Mock
  private PerfilRepository perfilRepository;

  @Mock
  private RegistrationTokenRepository registrationTokenRepository;

  @Mock
  private RegistrationEmailService registrationEmailService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private RegistrationFlowService registrationFlowService;

  @BeforeEach
  void setup() {
    ReflectionTestUtils.setField(registrationFlowService, "expirationHours", 2L);
    ReflectionTestUtils.setField(registrationFlowService, "frontendConfirmUrl", "http://localhost:4200/#/confirmar-cadastro");
    ReflectionTestUtils.setField(registrationFlowService, "hashSecret", "test-secret");
  }

  @Test
  void shouldInitiateSignupAndSendEmail() {
    SignupInitDTO request = new SignupInitDTO();
    request.setUsername("novo");
    request.setEmail("novo@teste.com");

    Perfil userRole = new Perfil();
    userRole.setNome(EPerfil.ROLE_USER);

    Usuario persisted = Usuario.builder().id(10L).username("novo").email("novo@teste.com").roles(Set.of(userRole)).build();

    when(usuarioRepository.existsByUsername("novo")).thenReturn(false);
    when(usuarioRepository.existsByEmail("novo@teste.com")).thenReturn(false);
    when(perfilRepository.findByNome(EPerfil.ROLE_USER)).thenReturn(Optional.of(userRole));
    when(passwordEncoder.encode(any())).thenReturn("encoded-temp");
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(persisted);

    registrationFlowService.initiateSignup(request);

    verify(registrationTokenRepository).save(any(RegistrationToken.class));
    verify(registrationEmailService).sendConfirmationEmail(any(), any(), any(), any(Long.class));
  }

  @Test
  void shouldCompleteRegistrationWhenTokenIsValid() {
    Usuario usuario = Usuario.builder().id(1L).username("user").email("user@teste.com").emailConfirmed(false).build();

    RegistrationToken token = RegistrationToken.builder()
        .id(1L)
        .usuario(usuario)
        .purpose(RegistrationTokenPurpose.EMAIL_CONFIRMATION)
        .tokenHash("hash")
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusMinutes(15))
        .build();

    when(registrationTokenRepository.findByTokenHashAndPurposeAndUsedAtIsNull(any(), any())).thenReturn(Optional.of(token));
    when(passwordEncoder.encode("Senha@123")).thenReturn("encoded-pass");

    registrationFlowService.completeRegistration("raw-token", "Senha@123", "Senha@123");

    ArgumentCaptor<Usuario> userCaptor = ArgumentCaptor.forClass(Usuario.class);
    verify(usuarioRepository).save(userCaptor.capture());
    assertTrue(userCaptor.getValue().isEmailConfirmed());
    verify(registrationTokenRepository).save(token);
  }

  @Test
  void shouldRejectExpiredToken() {
    RegistrationToken expired = RegistrationToken.builder()
        .id(1L)
        .usuario(Usuario.builder().id(1L).username("user").build())
        .purpose(RegistrationTokenPurpose.EMAIL_CONFIRMATION)
        .tokenHash("hash")
        .createdAt(LocalDateTime.now().minusHours(3))
        .expiresAt(LocalDateTime.now().minusMinutes(1))
        .build();

    when(registrationTokenRepository.findByTokenHashAndPurposeAndUsedAtIsNull(any(), any())).thenReturn(Optional.of(expired));

    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> registrationFlowService.validateToken("expired"));

    assertTrue(ex.getReason().contains("expirou"));
    verify(usuarioRepository, never()).save(any());
  }

  @Test
  void shouldIgnoreResendForUnknownEmail() {
    when(usuarioRepository.findByEmail("naoexiste@teste.com")).thenReturn(Optional.empty());

    registrationFlowService.resendConfirmation("naoexiste@teste.com");

    verify(registrationEmailService, never()).sendConfirmationEmail(any(), any(), any(), any(Long.class));
  }

  @Test
  void shouldRejectWeakPasswordOnCompleteRegistration() {
    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> registrationFlowService.completeRegistration("raw-token", "senhafraca", "senhafraca"));

    assertTrue(ex.getReason().contains("caractere especial"));
    verify(registrationTokenRepository, never()).findByTokenHashAndPurposeAndUsedAtIsNull(any(), any());
  }

  @Test
  void shouldRejectDifferentPasswordConfirmation() {
    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> registrationFlowService.completeRegistration("raw-token", "Senha@123", "Senha@124"));

    assertTrue(ex.getReason().contains("nao conferem"));
    verify(registrationTokenRepository, never()).findByTokenHashAndPurposeAndUsedAtIsNull(any(), any());
  }
}


