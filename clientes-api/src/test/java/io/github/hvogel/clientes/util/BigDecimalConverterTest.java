package io.github.hvogel.clientes.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class BigDecimalConverterTest {

    private final BigDecimalConverter converter = new BigDecimalConverter();

    @Test
    void testConverter_Success() {
        BigDecimal result = converter.converter("1.234,56");
        assertEquals(new BigDecimal("1234.56"), result);
    }

    @Test
    void testConverter_WithoutThousandsSeparator() {
        BigDecimal result = converter.converter("123,45");
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void testConverter_Null() {
        BigDecimal result = converter.converter(null);
        assertNull(result);
    }

    @Test
    void testConverter_IntegerValue() {
        BigDecimal result = converter.converter("100");
        assertEquals(new BigDecimal("100"), result);
    }
}
