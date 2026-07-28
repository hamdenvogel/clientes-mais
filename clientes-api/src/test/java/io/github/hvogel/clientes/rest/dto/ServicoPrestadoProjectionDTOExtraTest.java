package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.util.EqualsTestHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServicoPrestadoProjectionDTOExtraTest {

    @Test
    void testEqualsAndHashCodeWithNulls() {
        ServicoPrestadoProjectionDTO dto1 = new ServicoPrestadoProjectionDTO();
        ServicoPrestadoProjectionDTO dto2 = new ServicoPrestadoProjectionDTO();

        // Both null
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        // One field null in one
        dto1.setDescricao("desc");
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());

        // Same description, others null
        dto2.setDescricao("desc");
        assertEquals(dto1, dto2);

        // id null in one
        dto1.setId(1);
        assertNotEquals(dto1, dto2);
        dto2.setId(1);
        assertEquals(dto1, dto2);

        // nome null in one
        dto1.setNome("nome");
        assertNotEquals(dto1, dto2);
        dto2.setNome("nome");
        assertEquals(dto1, dto2);

        // pix null in one
        dto1.setPix("pix");
        assertNotEquals(dto1, dto2);
        dto2.setPix("pix");
        assertEquals(dto1, dto2);
    }

    @Test
    void testEqualsEdgeCases() {
        ServicoPrestadoProjectionDTO dto = new ServicoPrestadoProjectionDTO("desc", 1, "nome", "pix");

        // Test standard edge cases
        EqualsTestHelper.assertEqualsEdgeCases(dto);

        // Different values
        assertNotEquals(dto, new ServicoPrestadoProjectionDTO("other", 1, "nome", "pix"));
        assertNotEquals(dto, new ServicoPrestadoProjectionDTO("desc", 2, "nome", "pix"));
        assertNotEquals(dto, new ServicoPrestadoProjectionDTO("desc", 1, "other", "pix"));
        assertNotEquals(dto, new ServicoPrestadoProjectionDTO("desc", 1, "nome", "other"));
    }
}
