package io.github.hvogel.clientes.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Vector;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ReadListener;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;

class HttpServletReqUtilTest {

    private HttpServletReqUtil reqUtil;

    @BeforeEach
    void setUp() {
        reqUtil = new HttpServletReqUtil();
    }

    // ... existing masking tests ...

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "{\"username\":\"user\", \"password\":\"secret123\"} | {\"username\":\"user\", \"password\":\"*****\"}",
            "{\"login\":\"user\", \"senha\":\"secret123\"} | {\"login\":\"user\", \"senha\":\"*****\"}",
            "{\"token\":\"abc-123-xyz\"} | {\"token\":\"*****\"}",
            "{\"access_token\":\"abc-123-xyz\"} | {\"access_token\":\"*****\"}",
            "{\"username\":\"user\", \"password\":\"123\", \"token\":\"xyz\"} | {\"username\":\"user\", \"password\":\"*****\", \"token\":\"*****\"}",
            "{\"password\" :   \"secret\"} | {\"password\" :   \"*****\"}"
    })
    void shouldMaskSensitiveFields(String input, String expected) {
        assertEquals(expected, HttpServletReqUtil.maskSensitiveData(input));
    }

    @Test
    void shouldNotMaskNonSensitiveData() {
        String json = "{\"name\":\"John\", \"email\":\"john@example.com\"}";
        String masked = HttpServletReqUtil.maskSensitiveData(json);
        assertEquals("{\"name\":\"John\", \"email\":\"john@example.com\"}", masked);
    }

    @Test
    void shouldHandleEmptyAndNullContent() {
        assertEquals("", HttpServletReqUtil.maskSensitiveData(""));
        assertEquals("", HttpServletReqUtil.maskSensitiveData(null));
    }

    @Test
    void testGetRemoteAddress_XForwardedFor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn("10.0.0.1");

        String ip = reqUtil.getRemoteAddress(request);
        assertEquals("10.0.0.1", ip);
    }

    @Test
    void testGetRemoteAddress_RemoteAddr() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        String ip = reqUtil.getRemoteAddress(request);
        assertEquals("192.168.1.1", ip);
    }

    @Test
    void testGetPayLoad_Post() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("POST");
        String jsonBody = "{\"username\":\"user\", \"password\":\"secret\"}";

        ServletInputStream servletInputStream = createMockServletInputStream(jsonBody);

        when(request.getInputStream()).thenReturn(servletInputStream);

        String payload = reqUtil.getPayLoad(request);
        assertEquals("{\"username\":\"user\", \"password\":\"*****\"}", payload);
    }

    @Test
    void testGetPayLoad_Put() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("PUT");
        String jsonBody = "{\"token\":\"abc-123\"}";

        ServletInputStream servletInputStream = createMockServletInputStream(jsonBody);

        when(request.getInputStream()).thenReturn(servletInputStream);

        String payload = reqUtil.getPayLoad(request);
        assertEquals("{\"token\":\"*****\"}", payload);
    }

    @Test
    void testGetPayLoad_Get() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");

        String payload = reqUtil.getPayLoad(request);
        assertEquals("Not a POST or PUT method", payload);
    }

    @Test
    void testGetRequestParams() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Vector<String> paramNames = new Vector<>();
        paramNames.add("username");
        paramNames.add("password");
        Enumeration<String> enumeration = paramNames.elements();

        when(request.getParameterNames()).thenReturn(enumeration);
        when(request.getParameter("username")).thenReturn("user1");
        when(request.getParameter("password")).thenReturn("secret123");

        String params = reqUtil.getRequestParams(request);
        // Expecting password to be masked to *****
        // Note: The order depends on how parameters are iterated, usually predictable
        // with Vector
        assertTrue(params.contains("username: user1"));
        assertTrue(params.contains("password: *****"));
    }

    @Test
    void testGetPayLoad_IOException() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("POST");
        when(request.getInputStream()).thenThrow(new IOException("Stream error"));

        String payload = reqUtil.getPayLoad(request);
        assertEquals("", payload);
    }

    private ServletInputStream createMockServletInputStream(String content) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }
}
