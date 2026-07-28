package io.github.hvogel.clientes.filter;

import io.github.hvogel.clientes.service.JwtService;
import io.github.hvogel.clientes.service.impl.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JwtAuthFilter(jwtService, usuarioService);
    }

    @Test
    void testDoFilterInternalValidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token.here");
        when(jwtService.tokenValido("valid.token.here")).thenReturn(true);
        when(jwtService.obterLoginUsuario("valid.token.here")).thenReturn("user");
        when(usuarioService.loadUserByUsername("user")).thenReturn(userDetails);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(usuarioService).loadUserByUsername("user");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalInvalidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtService.tokenValido("invalid.token")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalNoHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, never()).tokenValido(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalNotBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic token");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, never()).tokenValido(anyString());
        verify(filterChain).doFilter(request, response);
    }
}
