package io.github.hvogel.clientes.model.mapper;

import io.github.hvogel.clientes.model.entity.Equipamento;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.EquipamentoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipamentoMapperTest {

    private final EquipamentoMapper mapper = EquipamentoMapper.INSTANCE;

    @Test
    void testToDtoWithNull() {
        EquipamentoDTO result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    void testToDto() {
        Equipamento equipamento = new Equipamento();
        equipamento.setId(1L);
        equipamento.setDescricao("Equipamento Teste");
        
        ServicoPrestado servicoPrestado = new ServicoPrestado();
        servicoPrestado.setId(100);
        equipamento.setServicoPrestado(servicoPrestado);

        EquipamentoDTO result = mapper.toDto(equipamento);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("Equipamento Teste", result.getDescricao());
        assertEquals(Integer.valueOf(100), result.getServicoPrestadoId());
    }

    @Test
    void testToEntityWithNull() {
        Equipamento result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void testToEntity() {
        EquipamentoDTO dto = new EquipamentoDTO();
        dto.setId(1L);
        dto.setDescricao("Equipamento DTO");
        dto.setServicoPrestadoId(200);

        Equipamento result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("Equipamento DTO", result.getDescricao());
        assertNotNull(result.getServicoPrestado());
        assertEquals(Integer.valueOf(200), result.getServicoPrestado().getId());
    }

    @Test
    void testToDtoWithNullServicoPrestado() {
        Equipamento equipamento = new Equipamento();
        equipamento.setId(1L);
        equipamento.setDescricao("Sem servico");
        equipamento.setServicoPrestado(null);

        EquipamentoDTO result = mapper.toDto(equipamento);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getServicoPrestadoId());
    }
}
