package io.github.hvogel.clientes.model.mapper;

import io.github.hvogel.clientes.model.entity.Natureza;
import io.github.hvogel.clientes.rest.dto.NaturezaDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NaturezaMapperTest {

    private final NaturezaMapper mapper = NaturezaMapper.INSTANCE;

    @Test
    void testToDtoWithNull() {
        NaturezaDTO result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    void testToDto() {
        Natureza natureza = new Natureza();
        natureza.setId(1L);
        natureza.setDescricao("Natureza Teste");

        NaturezaDTO result = mapper.toDto(natureza);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Natureza Teste", result.getDescricao());
    }

    @Test
    void testToEntityWithNull() {
        Natureza result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void testToEntity() {
        NaturezaDTO dto = new NaturezaDTO();
        dto.setId(1L);
        dto.setDescricao("Natureza DTO");

        Natureza result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Natureza DTO", result.getDescricao());
    }
}
