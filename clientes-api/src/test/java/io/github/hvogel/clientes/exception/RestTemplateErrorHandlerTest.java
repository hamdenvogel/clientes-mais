package io.github.hvogel.clientes.exception;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;

class RestTemplateErrorHandlerTest {

    private RestTemplateErrorHandler handler = new RestTemplateErrorHandler();

    @Test
    void testHasError() throws IOException {
        ClientHttpResponse response = mock(ClientHttpResponse.class);

        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        assertFalse(handler.hasError(response));

        when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        assertTrue(handler.hasError(response));

        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        assertTrue(handler.hasError(response));
    }

    @Test
    void testHandleError_NotFound() throws IOException {
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);

        assertThrows(RegraNegocioException.class, () -> handler.handleError(response));
    }

    @Test
    void testHandleError_Other4xx() throws IOException {
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        // Verify that no exception is thrown for other 4xx errors as per implementation
        assertDoesNotThrow(() -> handler.handleError(response));
    }

    @Test
    void testHandleError_5xx() throws IOException {
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        // when(response.getStatusText()).thenReturn("Internal Server Error"); // Not
        // needed if not called by exception constructor used

        assertThrows(HttpClientErrorException.class, () -> handler.handleError(response));
    }
}
