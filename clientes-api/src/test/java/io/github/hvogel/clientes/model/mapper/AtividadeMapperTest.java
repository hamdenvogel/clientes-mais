package io.github.hvogel.clientes.model.mapper;

import io.github.hvogel.clientes.model.entity.Atividade;
import io.github.hvogel.clientes.rest.dto.AtividadeDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AtividadeMapperTest {

    private final AtividadeMapper mapper = AtividadeMapper.INSTANCE;

    @Test
    void testToDtoWithNull() {
        AtividadeDTO result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    void testToDto() {
        Atividade atividade = new Atividade();
        atividade.setId(1L);
        atividade.setDescricao("Atividade Teste");

        AtividadeDTO result = mapper.toDto(atividade);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Atividade Teste", result.getDescricao());
    }

    @Test
    void testToEntityWithNull() {
        Atividade result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void testToEntity() {
        AtividadeDTO dto = new AtividadeDTO();
        dto.setId(1L);
        dto.setDescricao("Atividade DTO");

        Atividade result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Atividade DTO", result.getDescricao());
    }
}
