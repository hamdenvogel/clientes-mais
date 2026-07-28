package io.github.hvogel.clientes.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Date;

import org.junit.jupiter.api.Test;

class DateUtilsTest {

    @Test
    void testFromString_Success() {
        Date result = DateUtils.fromString("01/01/2023");
        assertNotNull(result);
    }

    @Test
    void testFromString_Null() {
        Date result = DateUtils.fromString(null);
        assertNull(result);
    }

    @Test
    void testFromString_Empty() {
        Date result = DateUtils.fromString("");
        assertNull(result);
    }

    @Test
    void testFromString_AtEndOfDay() {
        Date result = DateUtils.fromString("01/01/2023", true);
        assertNotNull(result);
    }

    @Test
    void testFromString_AtStartOfDay() {
        Date result = DateUtils.fromString("01/01/2023", false);
        assertNotNull(result);
    }

    @Test
    void testHoje_AtEndOfDay() {
        Date result = DateUtils.hoje(true);
        assertNotNull(result);
    }

    @Test
    void testHoje_AtStartOfDay() {
        Date result = DateUtils.hoje(false);
        assertNotNull(result);
    }

    @Test
    void testDataInicioPadrao() {
        assertNotNull(DateUtils.getDataInicioPadrao());
    }

    @Test
    void testPrivateConstructor() throws Exception {
        java.lang.reflect.Constructor<DateUtils> constructor = DateUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(java.lang.reflect.InvocationTargetException.class, constructor::newInstance);
    }
}
