package io.github.hvogel.clientes.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.service.ValidadorService;

/**
 * Unit test for ValidadorController to ensure coverage.
 */
@ExtendWith(MockitoExtension.class)
class ValidadorControllerUnitTest {

    @Mock
    private ValidadorService validadorService;

    private ValidadorController controller;

    @BeforeEach
    void setUp() {
        controller = new ValidadorController(validadorService);
    }

    @Test
    void testValidarInteger_Success() {
        InfoResponseDTO response = controller.validarInteger("123");

        assertNotNull(response);
        assertEquals("Valor Integer validado com sucesso.", response.getMensagem());
        verify(validadorService).validarValorInteger("123");
    }

    @Test
    void testValidarInteger_Exception() {
        doThrow(new RegraNegocioException("Erro")).when(validadorService).validarValorInteger(anyString());

        InfoResponseDTO response = controller.validarInteger("invalid");

        assertNotNull(response);
        assertEquals("Erro", response.getMensagem());
    }
}
