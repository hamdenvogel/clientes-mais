package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.github.hvogel.clientes.model.entity.Usuario;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "expiracao", "30");
        ReflectionTestUtils.setField(jwtService, "chaveAssinatura", "bWluaGEtY2hhdmUtc2VjcmV0YS1wYXJhLWp3dC10ZXN0ZQ=="); // base64
                                                                                                                         // encoded
                                                                                                                         // "minha-chave-secreta-para-jwt-teste"
    }

    @Test
    void testGerarToken() {
        Usuario usuario = Usuario.builder().username("testuser").build();
        String token = jwtService.gerarToken(usuario);
        assertNotNull(token);
    }

    @Test
    void testTokenValido() {
        Usuario usuario = Usuario.builder().username("testuser").build();
        String token = jwtService.gerarToken(usuario);
        assertTrue(jwtService.tokenValido(token));
    }

    @Test
    void testTokenInvalido() {
        assertFalse(jwtService.tokenValido("invalid-token"));
    }

    @Test
    void testObterLoginUsuario() {
        Usuario usuario = Usuario.builder().username("testuser").build();
        String token = jwtService.gerarToken(usuario);
        assertEquals("testuser", jwtService.obterLoginUsuario(token));
    }

    @Test
    void testObterTempoExpiracao() {
        Usuario usuario = Usuario.builder().username("testuser").build();
        String token = jwtService.gerarToken(usuario);
        LocalDateTime expiracao = jwtService.obterTempoExpiracao(token);
        assertNotNull(expiracao);
        assertTrue(expiracao.isAfter(LocalDateTime.now()));
    }

    @Test
    void testObterTempoExpiracao_InvalidToken() {
        LocalDateTime expiracao = jwtService.obterTempoExpiracao("invalid-token");
        assertNull(expiracao);
    }

    @Test
    void testObterTempoExpiracaoEmSegundos() {
        Integer segundos = jwtService.obterTempoExpiracaoEmSegundos();
        assertEquals(1800, segundos); // 30 * 60
    }

    @Test
    void testObterTempoExpiracaoEmSegundos_InvalidFormat() {
        ReflectionTestUtils.setField(jwtService, "expiracao", "invalid");
        Integer segundos = jwtService.obterTempoExpiracaoEmSegundos();
        assertNull(segundos);
    }
}
