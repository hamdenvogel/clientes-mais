package io.github.hvogel.clientes.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import io.github.hvogel.clientes.service.impl.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private String jwtSecret = "thisIsAVeryLongSecretKeyForTestingPurposesThatShouldBeAtLeast512BitsLong!!!";
    private int jwtExpirationMs = 60; // minutes
    private SecretKey signingKey;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "chaveAssinatura", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "expiracao", jwtExpirationMs);
        signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Test
    void testGerarToken() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "test@email.com", "password",
                java.util.Collections.emptyList());
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.gerarToken(authentication);

        assertNotNull(token);
        assertTrue(jwtUtils.validarToken(token));
        assertEquals("testuser", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    void testValidarToken_Valid() {
        String token = Jwts.builder()
                .subject("testuser")
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 600000))
                .signWith(signingKey)
                .compact();

        assertTrue(jwtUtils.validarToken(token));
    }

    @Test
    void testValidarToken_InvalidSignature() {
        SecretKey wrongKey = Keys.hmacShaKeyFor(
                "anotherVeryLongSecretKeyForTestingPurposesThatShouldBeAtLeast512BitsLong!!!".getBytes());
        String token = Jwts.builder()
                .subject("testuser")
                .signWith(wrongKey)
                .compact();

        assertFalse(jwtUtils.validarToken(token));
    }

    @Test
    void testValidarToken_Malformed() {
        assertFalse(jwtUtils.validarToken("invalid.token.string"));
    }

    @Test
    void testValidarToken_Expired() {
        String token = Jwts.builder()
                .subject("testuser")
                .issuedAt(new Date(System.currentTimeMillis() - 20000))
                .expiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(signingKey)
                .compact();

        assertFalse(jwtUtils.validarToken(token));
    }

    @Test
    void testValidarToken_Unsupported() {
        // A plaintext JWT (no signature) might trigger UnsupportedJwtException
        String plaintextToken = Jwts.builder().subject("testuser").compact();
        assertFalse(jwtUtils.validarToken(plaintextToken));

        // A common way to cause IllegalArgumentException is null or empty
        assertFalse(jwtUtils.validarToken(null));
        assertFalse(jwtUtils.validarToken(""));
    }
}
