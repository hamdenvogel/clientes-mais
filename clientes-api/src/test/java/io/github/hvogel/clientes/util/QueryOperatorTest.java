package io.github.hvogel.clientes.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.github.hvogel.clientes.enums.QueryOperator;

class QueryOperatorTest {

    @Test
    void testEnumValues() {
        assertNotNull(QueryOperator.values());
        assertEquals(6, QueryOperator.values().length);
    }

    @Test
    void testValueOf() {
        assertEquals(QueryOperator.GREATER_THAN, QueryOperator.valueOf("GREATER_THAN"));
        assertEquals(QueryOperator.LESS_THAN, QueryOperator.valueOf("LESS_THAN"));
        assertEquals(QueryOperator.EQUALS, QueryOperator.valueOf("EQUALS"));
        assertEquals(QueryOperator.LIKE, QueryOperator.valueOf("LIKE"));
        assertEquals(QueryOperator.NOT_EQ, QueryOperator.valueOf("NOT_EQ"));
        assertEquals(QueryOperator.IN, QueryOperator.valueOf("IN"));
    }
}
