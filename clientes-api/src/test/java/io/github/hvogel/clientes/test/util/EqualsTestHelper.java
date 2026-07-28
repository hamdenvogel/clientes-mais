package io.github.hvogel.clientes.test.util;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Helper class for testing equals() and hashCode() edge cases.
 */
public final class EqualsTestHelper {

    private EqualsTestHelper() {
        // Utility class
    }

    /**
     * Tests standard edge cases for equals() method.
     *
     * @param object The object to test
     */
    public static void assertEqualsEdgeCases(Object object) {
        // Same instance
        assertEquals(object, object);

        // Null
        assertNotEquals(null, object);

        // Different class
        assertFalse(object.equals("not same class"));
    }
}
