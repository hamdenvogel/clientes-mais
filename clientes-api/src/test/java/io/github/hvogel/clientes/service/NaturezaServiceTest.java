package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.entity.Natureza;
import io.github.hvogel.clientes.model.mapper.NaturezaMapper;
import io.github.hvogel.clientes.model.repository.NaturezaRepository;
import io.github.hvogel.clientes.rest.dto.NaturezaDTO;

@ExtendWith(MockitoExtension.class)
class NaturezaServiceTest {

    @Mock
    private NaturezaRepository repository;

    @Mock
    private NaturezaMapper mapper;

    @InjectMocks
    private NaturezaService service;

    @Test
    void testConvertToDto() {
        Natureza entity = new Natureza();
        entity.setId(1L);
        entity.setDescricao("Test");

        NaturezaDTO dto = new NaturezaDTO();
        dto.setId(1L);
        dto.setDescricao("Test");

        when(mapper.toDto(any(Natureza.class))).thenReturn(dto);

        NaturezaDTO result = service.convertToDto(entity);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testConvertToEntity() {
        NaturezaDTO dto = new NaturezaDTO();
        dto.setId(1L);
        dto.setDescricao("Test");

        Natureza entity = new Natureza();
        entity.setId(1L);
        entity.setDescricao("Test");

        when(mapper.toEntity(any(NaturezaDTO.class))).thenReturn(entity);

        Natureza result = service.convertToEntity(dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testObterPorId() {
        Natureza entity = new Natureza();
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Natureza> result = service.obterPorId(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }
}
