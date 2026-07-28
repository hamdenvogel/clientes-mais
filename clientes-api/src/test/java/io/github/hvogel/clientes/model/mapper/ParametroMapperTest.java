package io.github.hvogel.clientes.model.mapper;

import io.github.hvogel.clientes.model.entity.Parametro;
import io.github.hvogel.clientes.rest.dto.ParametroDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParametroMapperTest {

    private final ParametroMapper mapper = ParametroMapper.INSTANCE;

    @Test
    void testToDtoWithNull() {
        ParametroDTO result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    void testToDto() {
        Parametro parametro = new Parametro();
        parametro.setId(1L);
        parametro.setDescricao("Parametro Teste");

        ParametroDTO result = mapper.toDto(parametro);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());
        assertEquals("Parametro Teste", result.getDescricao());
    }

    @Test
    void testToEntityWithNull() {
        Parametro result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void testToEntity() {
        ParametroDTO dto = new ParametroDTO();
        dto.setId(1);
        dto.setDescricao("Parametro DTO");

        Parametro result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Parametro DTO", result.getDescricao());
    }
}
