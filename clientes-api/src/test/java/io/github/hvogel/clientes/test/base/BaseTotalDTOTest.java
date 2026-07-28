package io.github.hvogel.clientes.test.base;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Base test class for testing DTOs with a single total field.
 * @param <T> The DTO type
 */
public abstract class BaseTotalDTOTest<T> {

    /**
     * Creates a DTO with the given total value.
     * @param total The total value
     * @return The created DTO
     */
    protected abstract T createDTO(Long total);

    /**
     * Gets the total value from the DTO.
     * @param dto The DTO
     * @return The total value
     */
    protected abstract Long getTotal(T dto);

    /**
     * Sets the total value on the DTO.
     * @param dto The DTO
     * @param total The total value to set
     */
    protected abstract void setTotal(T dto, Long total);

    @Test
    void testBuilder() {
        T dto = createDTO(75L);
        assertNotNull(dto);
        assertEquals(75L, getTotal(dto));
    }

    @Test
    void testBuilderZero() {
        T dto = createDTO(0L);
        assertNotNull(dto);
        assertEquals(0L, getTotal(dto));
    }

    @Test
    void testSetters() {
        T dto = createDTO(30L);
        setTotal(dto, 150L);
        assertEquals(150L, getTotal(dto));
    }

    @Test
    void testEquals() {
        T dto1 = createDTO(75L);
        T dto2 = createDTO(75L);
        T dto3 = createDTO(150L);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1, dto1);
        assertNotEquals(null, dto1);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void testHashCode() {
        T dto1 = createDTO(75L);
        T dto2 = createDTO(75L);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        T dto = createDTO(75L);
        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("75"));
    }
}
