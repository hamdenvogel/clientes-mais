package io.github.hvogel.clientes.model.mapper;

import io.github.hvogel.clientes.model.entity.Solucao;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.SolucaoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolucaoMapperTest {

    private final SolucaoMapper mapper = SolucaoMapper.INSTANCE;

    @Test
    void testToDtoWithNull() {
        SolucaoDTO result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    void testToDto() {
        Solucao solucao = new Solucao();
        solucao.setId(1L);
        solucao.setDescricao("Solucao Teste");
        
        ServicoPrestado servicoPrestado = new ServicoPrestado();
        servicoPrestado.setId(100);
        solucao.setServicoPrestado(servicoPrestado);

        SolucaoDTO result = mapper.toDto(solucao);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("Solucao Teste", result.getDescricao());
        assertEquals(Integer.valueOf(100), result.getServicoPrestadoId());
    }

    @Test
    void testToEntityWithNull() {
        Solucao result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void testToEntity() {
        SolucaoDTO dto = new SolucaoDTO();
        dto.setId(1L);
        dto.setDescricao("Solucao DTO");
        dto.setServicoPrestadoId(200);

        Solucao result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("Solucao DTO", result.getDescricao());
        assertNotNull(result.getServicoPrestado());
        assertEquals(Integer.valueOf(200), result.getServicoPrestado().getId());
    }

    @Test
    void testToDtoWithNullServicoPrestado() {
        Solucao solucao = new Solucao();
        solucao.setId(1L);
        solucao.setDescricao("Sem servico");
        solucao.setServicoPrestado(null);

        SolucaoDTO result = mapper.toDto(solucao);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getServicoPrestadoId());
    }
}
