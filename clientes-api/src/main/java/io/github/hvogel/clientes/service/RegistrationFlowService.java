package io.github.hvogel.clientes.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.enums.RegistrationTokenPurpose;
import io.github.hvogel.clientes.model.entity.Perfil;
import io.github.hvogel.clientes.model.entity.RegistrationToken;
import io.github.hvogel.clientes.model.entity.Usuario;
import io.github.hvogel.clientes.model.repository.PerfilRepository;
import io.github.hvogel.clientes.model.repository.RegistrationTokenRepository;
import io.github.hvogel.clientes.model.repository.UsuarioRepository;
import io.github.hvogel.clientes.rest.dto.RegistrationTokenValidationDTO;
import io.github.hvogel.clientes.rest.dto.SignupInitDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationFlowService {

  private static final Pattern STRONG_PASSWORD_PATTERN = Pattern
      .compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])\\S{8,60}$");

  private final UsuarioRepository usuarioRepository;
  private final PerfilRepository perfilRepository;
  private final RegistrationTokenRepository registrationTokenRepository;
  private final RegistrationEmailService registrationEmailService;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.registration.confirmation.expiration-hours:2}")
  private long expirationHours;

  @Value("${app.registration.frontend-confirm-url:http://localhost:4200/#/confirmar-cadastro}")
  private String frontendConfirmUrl;

  @Value("${app.registration.token.hash-secret:clientes-secret-token}")
  private String hashSecret;

  @Transactional
  public void initiateSignup(SignupInitDTO request) {
    if (usuarioRepository.existsByUsername(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este login ja esta em uso.");
    }

    if (usuarioRepository.existsByEmail(request.getEmail())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este e-mail ja esta em uso.");
    }

    Perfil userRole = perfilRepository.findByNome(EPerfil.ROLE_USER)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Perfil padrao nao encontrado."));

    Usuario usuario = Usuario.builder()
        .username(request.getUsername())
        .email(request.getEmail())
      .cpf(generateUniqueCpf())
        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
      .ativo(true)
        .emailConfirmed(false)
        .build();

    Set<Perfil> roles = new HashSet<>();
    roles.add(userRole);
    usuario.setRoles(roles);

    Usuario saved = usuarioRepository.save(usuario);
    String rawToken = createToken(saved);

    registrationEmailService.sendConfirmationEmail(
        saved.getUsername(),
        saved.getEmail(),
        buildConfirmLink(rawToken),
        expirationHours);
  }

  @Transactional(readOnly = true)
  public RegistrationTokenValidationDTO validateToken(String token) {
    RegistrationToken registrationToken = findValidToken(token);

    return RegistrationTokenValidationDTO.builder()
        .valid(true)
        .message("Token valido. Voce ja pode definir sua senha.")
        .expiresAt(registrationToken.getExpiresAt())
        .build();
  }

  @Transactional
  public void completeRegistration(String token, String password, String confirmPassword) {
    validatePassword(password, confirmPassword);

    RegistrationToken registrationToken = findValidToken(token);

    Usuario usuario = registrationToken.getUsuario();
    usuario.setPassword(passwordEncoder.encode(password));
    usuario.setEmailConfirmed(true);
    usuario.setEmailConfirmedAt(LocalDateTime.now());
    usuarioRepository.save(usuario);

    registrationToken.setUsedAt(LocalDateTime.now());
    registrationTokenRepository.save(registrationToken);

    invalidateOpenTokens(usuario);
  }

  private void validatePassword(String password, String confirmPassword) {
    if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas informadas nao conferem.");
    }

    if (!STRONG_PASSWORD_PATTERN.matcher(password).matches()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "A senha deve conter de 8 a 60 caracteres, com letra maiuscula, minuscula, numero e caractere especial, sem espacos.");
    }
  }

  @Transactional
  public void resendConfirmation(String email) {
    usuarioRepository.findByEmail(email)
        .filter(user -> !user.isEmailConfirmed())
        .ifPresent(user -> {
          String rawToken = createToken(user);
          registrationEmailService.sendConfirmationEmail(
              user.getUsername(),
              user.getEmail(),
              buildConfirmLink(rawToken),
              expirationHours);
        });
  }

  private RegistrationToken findValidToken(String rawToken) {
    String tokenHash = hashToken(rawToken);
    RegistrationToken registrationToken = registrationTokenRepository
        .findByTokenHashAndPurposeAndUsedAtIsNull(tokenHash, RegistrationTokenPurpose.EMAIL_CONFIRMATION)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Link invalido ou ja utilizado."));

    if (registrationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este link expirou. Solicite um novo e-mail de confirmacao.");
    }

    return registrationToken;
  }

  private void invalidateOpenTokens(Usuario usuario) {
    List<RegistrationToken> openTokens = registrationTokenRepository
        .findByUsuarioAndPurposeAndUsedAtIsNull(usuario, RegistrationTokenPurpose.EMAIL_CONFIRMATION);

    LocalDateTime now = LocalDateTime.now();
    openTokens.forEach(item -> item.setUsedAt(now));
    registrationTokenRepository.saveAll(openTokens);
  }

  private String createToken(Usuario usuario) {
    invalidateOpenTokens(usuario);

    String rawToken = Base64.getUrlEncoder().withoutPadding()
        .encodeToString((UUID.randomUUID() + ":" + System.nanoTime()).getBytes(StandardCharsets.UTF_8));

    RegistrationToken registrationToken = RegistrationToken.builder()
        .usuario(usuario)
        .purpose(RegistrationTokenPurpose.EMAIL_CONFIRMATION)
        .tokenHash(hashToken(rawToken))
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusHours(expirationHours))
        .build();

    registrationTokenRepository.save(registrationToken);
    return rawToken;
  }

  private String buildConfirmLink(String rawToken) {
    return "%s?token=%s".formatted(frontendConfirmUrl, rawToken);
  }

  private String hashToken(String rawToken) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] hash = messageDigest.digest((rawToken + hashSecret).getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("Algoritmo de hash nao disponivel.", ex);
    }
  }

  private String generateUniqueCpf() {
    String cpf;
    do {
      long candidate = Math.abs(UUID.randomUUID().getMostSignificantBits()) % 100_000_000_000L;
      cpf = String.format("%011d", candidate);
    } while (usuarioRepository.existsByCpf(cpf));
    return cpf;
  }
}

