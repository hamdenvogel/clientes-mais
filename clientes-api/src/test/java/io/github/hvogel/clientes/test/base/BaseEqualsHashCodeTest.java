package io.github.hvogel.clientes.test.base;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Base test class for testing objects with equals, hashCode and toString.
 * Reduces code duplication by providing common test structure.
 * @param <T> The type being tested
 */
public abstract class BaseEqualsHashCodeTest<T> {

    /**
     * Creates an instance with default/sample values for testing.
     * @return Instance with test data
     */
    protected abstract T createInstance();

    /**
     * Creates another instance with the same values as createInstance().
     * @return Instance equal to createInstance()
     */
    protected abstract T createEqualInstance();

    /**
     * Creates an instance with different values.
     * @return Instance not equal to createInstance()
     */
    protected abstract T createDifferentInstance();

    @Test
    void testEqualsAndHashCode() {
        T instance1 = createInstance();
        T instance2 = createEqualInstance();
        T instance3 = createDifferentInstance();

        // Equal instances
        assertEquals(instance1, instance2);
        assertEquals(instance1.hashCode(), instance2.hashCode());

        // Different instances
        assertNotEquals(instance1, instance3);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    void testToString() {
        T instance = createInstance();
        assertNotNull(instance.toString());
    }
}
