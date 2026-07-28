package io.github.hvogel.clientes.model.mapper;

import io.github.hvogel.clientes.model.entity.Diagnostico;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.DiagnosticoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiagnosticoMapperTest {

    private final DiagnosticoMapper mapper = DiagnosticoMapper.INSTANCE;

    @Test
    void testToDtoWithNull() {
        DiagnosticoDTO result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    void testToDto() {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        diagnostico.setDescricao("Teste");
        
        ServicoPrestado servicoPrestado = new ServicoPrestado();
        servicoPrestado.setId(100);
        diagnostico.setServicoPrestado(servicoPrestado);

        DiagnosticoDTO result = mapper.toDto(diagnostico);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Teste", result.getDescricao());
        assertEquals(Integer.valueOf(100), result.getServicoPrestadoId());
    }

    @Test
    void testToEntityWithNull() {
        Diagnostico result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void testToEntity() {
        DiagnosticoDTO dto = new DiagnosticoDTO();
        dto.setId(1L);
        dto.setDescricao("Teste DTO");
        dto.setServicoPrestadoId(200);

        Diagnostico result = mapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Teste DTO", result.getDescricao());
        assertNotNull(result.getServicoPrestado());
        assertEquals(Integer.valueOf(200), result.getServicoPrestado().getId());
    }

    @Test
    void testToDtoWithNullServicoPrestado() {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        diagnostico.setDescricao("Sem servico");
        diagnostico.setServicoPrestado(null);

        DiagnosticoDTO result = mapper.toDto(diagnostico);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getServicoPrestadoId());
    }
}
