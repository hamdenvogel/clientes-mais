package io.github.hvogel.clientes.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

@ExtendWith(MockitoExtension.class)
class RequestResponseLoggingInterceptorTest {

    @InjectMocks
    private RequestResponseLoggingInterceptor interceptor;

    @Mock
    private HttpRequest request;

    @Mock
    private ClientHttpRequestExecution execution;

    @Mock
    private ClientHttpResponse response;

    @Test
    void testIntercept() throws IOException {
        byte[] body = "test body".getBytes(StandardCharsets.UTF_8);

        when(request.getURI()).thenReturn(URI.create("http://localhost:8080/api"));
        when(request.getMethod()).thenReturn(HttpMethod.POST);
        when(request.getHeaders()).thenReturn(new HttpHeaders());

        when(execution.execute(any(HttpRequest.class), eq(body))).thenReturn(response);

        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        when(response.getStatusText()).thenReturn("OK");
        when(response.getHeaders()).thenReturn(new HttpHeaders());
        when(response.getBody()).thenReturn(new ByteArrayInputStream("response body".getBytes(StandardCharsets.UTF_8)));

        ClientHttpResponse result = interceptor.intercept(request, body, execution);

        assertEquals(response, result);
        verify(execution, times(1)).execute(request, body);
        verify(request, times(1)).getURI(); // Verification that logging was attempted
    }
}
