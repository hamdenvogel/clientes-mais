package io.github.hvogel.clientes.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.github.hvogel.clientes.service.impl.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${security.jwt.chave-assinatura}")
  private String chaveAssinatura;

  @Value("${security.jwt.expiracao}")
  private int expiracao;

  private SecretKey getSigningKey() {
    byte[] keyBytes;
    try {
      keyBytes = java.util.Base64.getDecoder().decode(chaveAssinatura);
      if (keyBytes.length < 32) { // Minimum 256 bits for HS256
        keyBytes = chaveAssinatura.getBytes();
      }
    } catch (IllegalArgumentException e) {
      keyBytes = chaveAssinatura.getBytes();
    }
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String gerarToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    long expString = expiracao;
    LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
    Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
    Date data = Date.from(instant);

    String authorities = userPrincipal.getAuthorities().stream()
        .map(auth -> auth.getAuthority())
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .subject(userPrincipal.getUsername())
        .issuedAt(new Date())
        .expiration(data)
        .claim("authorities", authorities)
        .claim("id", userPrincipal.getId())
        .claim("email", userPrincipal.getEmail())
        .signWith(getSigningKey())
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public boolean validarToken(String authToken) {
    try {
      Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    } catch (Exception e) {
      logger.error("Unexpected error validating JWT: {}", e.getMessage());
    }

    return false;
  }
}
