package io.github.hvogel.clientes.security.jwt;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String token = "valid.token.jwt";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validarToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assert (SecurityContextHolder.getContext().getAuthentication() != null);
        assert (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(userDetails));
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "invalid.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validarToken(token)).thenReturn(false);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_Exception() throws ServletException, IOException {
        String token = "valid.token.jwt";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.validarToken(token)).thenThrow(new io.jsonwebtoken.MalformedJwtException("Error"));

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
