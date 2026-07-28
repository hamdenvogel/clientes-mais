package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NaturezaDTOTest extends BaseEqualsHashCodeTest<NaturezaDTO> {

    @Override
    protected NaturezaDTO createInstance() {
        NaturezaDTO dto = new NaturezaDTO();
        dto.setId(1L);
        dto.setDescricao("Natureza");
        return dto;
    }

    @Override
    protected NaturezaDTO createEqualInstance() {
        NaturezaDTO dto = new NaturezaDTO();
        dto.setId(1L);
        dto.setDescricao("Natureza");
        return dto;
    }

    @Override
    protected NaturezaDTO createDifferentInstance() {
        NaturezaDTO dto = new NaturezaDTO();
        dto.setId(2L);
        return dto;
    }

    @Test
    void testGettersAndSetters() {
        NaturezaDTO dto = new NaturezaDTO();

        dto.setId(1L);
        dto.setDescricao("Natureza Teste");

        assertEquals(1L, dto.getId());
        assertEquals("Natureza Teste", dto.getDescricao());
    }
}
