package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.entity.Parametro;
import io.github.hvogel.clientes.model.mapper.ParametroMapper;
import io.github.hvogel.clientes.model.repository.ParametroRepository;
import io.github.hvogel.clientes.rest.dto.ParametroDTO;

@ExtendWith(MockitoExtension.class)
class ParametroServiceTest {

    @Mock
    private ParametroRepository repository;

    @Mock
    private ParametroMapper mapper;

    @InjectMocks
    private ParametroService service;

    @Test
    void testConvertToDto() {
        Parametro entity = new Parametro();
        entity.setId(1L);
        entity.setDescricao("test_param");

        ParametroDTO dto = new ParametroDTO();
        dto.setId(1);
        dto.setDescricao("test_param");

        when(mapper.toDto(any(Parametro.class))).thenReturn(dto);

        ParametroDTO result = service.convertToDto(entity);
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testConvertToEntity() {
        ParametroDTO dto = new ParametroDTO();
        dto.setId(1);
        dto.setDescricao("test_param");

        Parametro entity = new Parametro();
        entity.setId(1L);
        entity.setDescricao("test_param");

        when(mapper.toEntity(any(ParametroDTO.class))).thenReturn(entity);

        Parametro result = service.convertToEntity(dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
