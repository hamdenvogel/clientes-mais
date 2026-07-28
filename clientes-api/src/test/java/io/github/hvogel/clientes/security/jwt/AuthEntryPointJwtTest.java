package io.github.hvogel.clientes.security.jwt;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthEntryPointJwtTest {

    private AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    @Test
    void testCommence_JsonRequest() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Unauthorized") {
        };

        authEntryPointJwt.commence(request, response, authException);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertTrue(response.getContentAsString().contains("Unauthorized"));
    }

    @Test
    void testCommence_HtmlRequest() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept", MediaType.TEXT_HTML_VALUE);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Unauthorized") {
        };

        authEntryPointJwt.commence(request, response, authException);

        assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, response.getStatus());
        assertEquals("/", response.getRedirectedUrl());
    }
}
