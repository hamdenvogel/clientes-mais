package io.github.hvogel.clientes.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.rest.exception.ApiErrors;

class ApplicationControllerAdviceTest {

    private ApplicationControllerAdvice advice = new ApplicationControllerAdvice();

    @Test
    void testHandleValidationErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        ObjectError error = new ObjectError("object", "default message");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(error));

        ApiErrors errors = advice.handleValidationErrors(ex);

        assertNotNull(errors);
        assertEquals(1, errors.getErrors().size());
        assertEquals("default message", errors.getErrors().getFirst());
    }

    @Test
    void testHandleResponseStatusException() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found reason");

        ResponseEntity<ApiErrors> response = advice.handleResponseStatusException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Not found reason", response.getBody().getErrors().getFirst());
    }
}
