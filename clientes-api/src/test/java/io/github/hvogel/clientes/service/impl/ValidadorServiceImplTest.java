package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.util.BigDecimalConverter;
import io.github.hvogel.clientes.util.ValidDouble;

@ExtendWith(MockitoExtension.class)
class ValidadorServiceImplTest {

    @Mock
    private ValidDouble validDouble;

    @Mock
    private BigDecimalConverter bigDecimalConverter;

    @InjectMocks
    private ValidadorServiceImpl service;

    @Test
    void testValidarData_Success() {
        assertDoesNotThrow(() -> service.validarData("01/01/2023"));
    }

    @Test
    void testValidarData_Invalid() {
        assertThrows(RegraNegocioException.class, () -> service.validarData("invalid-date"));
    }

    @Test
    void testValidarValorNumerico_Success() {
        when(validDouble.isNumericValid("10.00")).thenReturn(true);
        assertDoesNotThrow(() -> service.validarValorNumerico("10.00"));
    }

    @Test
    void testValidarValorNumerico_Invalid() {
        when(validDouble.isNumericValid("invalid")).thenReturn(false);
        assertThrows(RegraNegocioException.class, () -> service.validarValorNumerico("invalid"));
    }

    @Test
    void testConverter() {
        BigDecimal expected = new BigDecimal("10.00");
        when(bigDecimalConverter.converter("10,00")).thenReturn(expected);
        assertEquals(expected, service.converter("10,00"));
    }

    @Test
    void testValidarStatus_Success() {
        assertDoesNotThrow(() -> service.validarStatus("E")); // E is a valid status
    }

    @Test
    void testValidarStatus_Invalid() {
        assertThrows(RegraNegocioException.class, () -> service.validarStatus("INVALIDO"));
    }

    @Test
    void testValidarStatus_Null() {
        assertThrows(RegraNegocioException.class, () -> service.validarStatus(null));
    }

    @Test
    void testValidarCep_Success() {
        assertDoesNotThrow(() -> service.validarCep("12345678"));
    }

    @Test
    void testValidarCep_Invalid() {
        assertThrows(RegraNegocioException.class, () -> service.validarCep("12345-678"));
    }

    @Test
    void testValidarTokenGoogle_Success() {
        assertDoesNotThrow(() -> service.validarTokenGoogle("valid_token-123"));
    }

    @Test
    void testValidarTokenGoogle_Invalid() {
        assertThrows(RegraNegocioException.class, () -> service.validarTokenGoogle("invalid token!"));
    }

    @Test
    void testValidarValorInteger_Success() {
        assertDoesNotThrow(() -> service.validarValorInteger("123"));
    }

    @Test
    void testValidarValorInteger_Invalid() {
        assertThrows(RegraNegocioException.class, () -> service.validarValorInteger("12a"));
    }

    @Test
    void testValidarValorInteger_Empty() {
        assertThrows(RegraNegocioException.class, () -> service.validarValorInteger(""));
    }

    @Test
    void testValidarValorInteger_Negative() {
        // Assuming negative implies '-' at start, which implementation handles
        assertDoesNotThrow(() -> service.validarValorInteger("-123"));
    }

    @Test
    void testValidarValorInteger_JustMinus() {
        assertThrows(RegraNegocioException.class, () -> service.validarValorInteger("-"));
    }

    @Test
    void testValidarTipoServico_Success() {
        assertDoesNotThrow(() -> service.validarTipoServico("U"));
    }

    @Test
    void testValidarTipoServico_Invalid() {
        assertThrows(RegraNegocioException.class, () -> service.validarTipoServico("X"));
    }

    @Test
    void testValidarTipoPacote_Success() {
        assertDoesNotThrow(() -> service.validarTipoPacote("I"));
    }

    @Test
    void testValidarTipoPacote_Invalid() {
        assertThrows(RegraNegocioException.class, () -> service.validarTipoPacote("X"));
    }

    @Test
    void testMontaArrayTipoServicos() {
        String[] tipos = ValidadorServiceImpl.montaArrayTipoServicos();
        assertNotNull(tipos);
        assertTrue(tipos.length > 0);
    }

    @Test
    void testMontaArrayTipoPacotes() {
        String[] tipos = ValidadorServiceImpl.montaArrayTipoPacotes();
        assertNotNull(tipos);
        assertTrue(tipos.length > 0);
    }

    @Test
    void testValidarValorInteger_WithBase() {
        assertDoesNotThrow(() -> service.validarValorInteger("FF", 16));
    }

    @Test
    void testValidarValorInteger_WithBase_Invalid() {
        assertThrows(RegraNegocioException.class, () -> service.validarValorInteger("ZZ", 16));
    }

    @Test
    void testValidarTipoServico_P() {
        assertDoesNotThrow(() -> service.validarTipoServico("P"));
    }

    @Test
    void testValidarTipoPacote_AllValues() {
        assertDoesNotThrow(() -> service.validarTipoPacote("I"));
        assertDoesNotThrow(() -> service.validarTipoPacote("A"));
        assertDoesNotThrow(() -> service.validarTipoPacote("E"));
        assertDoesNotThrow(() -> service.validarTipoPacote("C"));
        assertDoesNotThrow(() -> service.validarTipoPacote("F"));
    }

    @Test
    void testValidarStatus_AllValidStatuses() {
        assertDoesNotThrow(() -> service.validarStatus("E"));
        assertDoesNotThrow(() -> service.validarStatus("C"));
        assertDoesNotThrow(() -> service.validarStatus("F"));
        assertDoesNotThrow(() -> service.validarStatus("P"));
    }

    @Test
    void testValidarCep_InvalidWithDash() {
        assertThrows(RegraNegocioException.class, () -> service.validarCep("12345-67"));
    }

    @Test
    void testValidarCep_TooShort() {
        assertThrows(RegraNegocioException.class, () -> service.validarCep("1234567"));
    }

    @Test
    void testValidarCep_TooLong() {
        assertThrows(RegraNegocioException.class, () -> service.validarCep("123456789"));
    }

    @Test
    void testValidarTokenGoogle_WithDash() {
        assertDoesNotThrow(() -> service.validarTokenGoogle("valid-token"));
    }

    @Test
    void testValidarTokenGoogle_WithUnderscore() {
        assertDoesNotThrow(() -> service.validarTokenGoogle("valid_token"));
    }

    @Test
    void testValidarTokenGoogle_WithSpace() {
        assertThrows(RegraNegocioException.class, () -> service.validarTokenGoogle("invalid token"));
    }

    @Test
    void testValidarTokenGoogle_WithSpecialChars() {
        assertThrows(RegraNegocioException.class, () -> service.validarTokenGoogle("invalid@token"));
    }

    @Test
    void testValidarValorInteger_Zero() {
        assertDoesNotThrow(() -> service.validarValorInteger("0"));
    }

    @Test
    void testValidarData_InvalidFormat() {
        assertThrows(RegraNegocioException.class, () -> service.validarData("2023-01-01"));
    }

    @Test
    void testValidarData_InvalidDate() {
        assertThrows(RegraNegocioException.class, () -> service.validarData("32/13/2023"));
    }

    private void assertTrue(boolean condition) {
        if (!condition)
            throw new AssertionError();
    }
}
