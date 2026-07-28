package io.github.hvogel.clientes.model.mapper;

import io.github.hvogel.clientes.model.entity.Chamado;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.rest.dto.ChamadoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChamadoMapperTest {

    private final ChamadoMapper mapper = ChamadoMapper.INSTANCE;

    @Test
    void testToDtoWithNull() {
        ChamadoDTO result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    void testToDto() {
        Chamado chamado = new Chamado();
        chamado.setId(1L);
        chamado.setDescricao("Chamado Teste");
        
        Cliente cliente = new Cliente();
        cliente.setId(50);
        chamado.setCliente(cliente);

        ChamadoDTO result = mapper.toDto(chamado);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Chamado Teste", result.getDescricao());
        assertEquals(Integer.valueOf(50), result.getClienteId());
    }

    @Test
    void testToEntityWithNull() {
        Chamado result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void testToEntity() {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setId(1L);
        dto.setDescricao("Chamado DTO");
        dto.setClienteId(75);

        Chamado result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Chamado DTO", result.getDescricao());
        assertNotNull(result.getCliente());
        assertEquals(Integer.valueOf(75), result.getCliente().getId());
    }

    @Test
    void testToDtoWithNullCliente() {
        Chamado chamado = new Chamado();
        chamado.setId(1L);
        chamado.setDescricao("Sem cliente");
        chamado.setCliente(null);

        ChamadoDTO result = mapper.toDto(chamado);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getClienteId());
    }
}
