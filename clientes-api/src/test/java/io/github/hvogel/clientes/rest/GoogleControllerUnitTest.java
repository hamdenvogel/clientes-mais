package io.github.hvogel.clientes.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import io.github.hvogel.clientes.rest.dto.GoogleRecaptchaDTO;
import io.github.hvogel.clientes.service.GoogleService;

/**
 * Unit test for GoogleController to ensure coverage.
 */
@ExtendWith(MockitoExtension.class)
class GoogleControllerUnitTest {

    @Mock
    private GoogleService googleService;

    private GoogleController controller;

    @BeforeEach
    void setUp() {
        controller = new GoogleController(googleService);
    }

    @Test
    void testValidarToken() {
        String token = "test-token";
        GoogleRecaptchaDTO mockResponse = new GoogleRecaptchaDTO();
        mockResponse.setSuccess(true);
        when(googleService.validarToken(token)).thenReturn(mockResponse);

        GoogleRecaptchaDTO response = controller.validarToken(token);

        assertNotNull(response);
        assertEquals(true, response.isSuccess());
        verify(googleService).validarToken(token);
    }

    @Test
    void testRemoverTentativas() {
        controller.removerTentativas();
        verify(googleService).zerarTentativasMalSucedidas();
    }

    @Test
    void testInformarValidacao() {
        // The controller implementation currently hardcodes this response
        // and does NOT call the service.
        io.github.hvogel.clientes.rest.dto.InfoResponseDTO result = controller.informarValidacao();

        assertNotNull(result);
        assertEquals("Favor validar o Captcha!", result.getMensagem());
        assertEquals("Informação", result.getTitulo());
    }
}
