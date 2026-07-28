package io.github.hvogel.clientes.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidDoubleTest {

    private final ValidDouble validDouble = new ValidDouble();

    @Test
    void testIsNumericValid_ValidInteger() {
        assertTrue(validDouble.isNumericValid("123"));
    }

    @Test
    void testIsNumericValid_ValidDecimalWithComma() {
        assertTrue(validDouble.isNumericValid("123,45"));
    }

    @Test
    void testIsNumericValid_ValidDecimalWithDot() {
        assertTrue(validDouble.isNumericValid("123.45"));
    }

    @Test
    void testIsNumericValid_InvalidString() {
        assertFalse(validDouble.isNumericValid("abc"));
    }

    @Test
    void testIsNumericValid_Null() {
        assertFalse(validDouble.isNumericValid(null));
    }

    @Test
    void testIsNumericValid_Empty() {
        assertFalse(validDouble.isNumericValid(""));
    }

    @Test
    void testIsNumericValid_NegativeNumber() {
        assertTrue(validDouble.isNumericValid("-123,45"));
    }
}
