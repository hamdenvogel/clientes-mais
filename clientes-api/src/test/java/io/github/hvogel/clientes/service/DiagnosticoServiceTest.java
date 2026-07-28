package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.model.entity.Diagnostico;
import io.github.hvogel.clientes.model.mapper.DiagnosticoMapper;
import io.github.hvogel.clientes.model.repository.DiagnosticoRepository;
import io.github.hvogel.clientes.rest.dto.DiagnosticoDTO;

@ExtendWith(MockitoExtension.class)
class DiagnosticoServiceTest {

    @Mock
    private DiagnosticoRepository repository;

    @Mock
    private DiagnosticoMapper mapper;

    private DiagnosticoService service;

    @BeforeEach
    void setUp() {
        service = new DiagnosticoService(repository, mapper);
    }

    @Test
    void testObterDiagnosticos() {
        Diagnostico diagnostico = new Diagnostico();
        when(repository.findAllByOrderByDescricaoAsc()).thenReturn(Arrays.asList(diagnostico));

        List<Diagnostico> result = service.obterDiagnosticos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByOrderByDescricaoAsc();
    }

    @Test
    void testObterPorId() {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(diagnostico));

        Optional<Diagnostico> result = service.obterPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindByServicoPrestadoId() {
        Page<Diagnostico> page = new PageImpl<>(Arrays.asList(new Diagnostico()));
        when(repository.findByServicoPrestadoId(anyInt(), any(Pageable.class))).thenReturn(page);

        Page<Diagnostico> result = service.findByServicoPrestadoId(1, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByServicoPrestadoId(anyInt(), any(Pageable.class));
    }

    @Test
    void testFindByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId() {
        Page<Diagnostico> page = new PageImpl<>(Arrays.asList(new Diagnostico()));
        when(repository.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class))).thenReturn(page);

        Page<Diagnostico> result = service.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId("teste", 1,
                Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class));
    }

    @Test
    void testCount() {
        when(repository.count()).thenReturn(10L);

        long result = service.count();

        assertEquals(10L, result);
        verify(repository, times(1)).count();
    }

    @Test
    void testConvertToDto() {
        Diagnostico entity = new Diagnostico();
        DiagnosticoDTO dto = new DiagnosticoDTO();
        when(mapper.toDto(entity)).thenReturn(dto);

        DiagnosticoDTO result = service.convertToDto(entity);

        assertEquals(dto, result);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    void testConvertToEntity() {
        DiagnosticoDTO dto = new DiagnosticoDTO();
        Diagnostico entity = new Diagnostico();
        when(mapper.toEntity(dto)).thenReturn(entity);

        Diagnostico result = service.convertToEntity(dto);

        assertEquals(entity, result);
        verify(mapper, times(1)).toEntity(dto);
    }
}
