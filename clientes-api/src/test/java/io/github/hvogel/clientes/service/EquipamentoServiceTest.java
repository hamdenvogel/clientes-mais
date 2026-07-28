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

import io.github.hvogel.clientes.model.entity.Equipamento;
import io.github.hvogel.clientes.model.mapper.EquipamentoMapper;
import io.github.hvogel.clientes.model.repository.EquipamentoRepository;
import io.github.hvogel.clientes.rest.dto.EquipamentoDTO;

@ExtendWith(MockitoExtension.class)
class EquipamentoServiceTest {

    @Mock
    private EquipamentoRepository repository;

    @Mock
    private EquipamentoMapper mapper;

    private EquipamentoService service;

    @BeforeEach
    void setUp() {
        service = new EquipamentoService(repository, mapper);
    }

    @Test
    void testObterEquipamentos() {
        Equipamento equipamento = new Equipamento();
        when(repository.findAllByOrderByDescricaoAsc()).thenReturn(Arrays.asList(equipamento));

        List<Equipamento> result = service.obterEquipamentos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByOrderByDescricaoAsc();
    }

    @Test
    void testObterPorId() {
        Equipamento equipamento = new Equipamento();
        equipamento.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(equipamento));

        Optional<Equipamento> result = service.obterPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindByServicoPrestadoId() {
        Page<Equipamento> page = new PageImpl<>(Arrays.asList(new Equipamento()));
        when(repository.findByServicoPrestadoId(anyInt(), any(Pageable.class))).thenReturn(page);

        Page<Equipamento> result = service.findByServicoPrestadoId(1, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByServicoPrestadoId(anyInt(), any(Pageable.class));
    }

    @Test
    void testFindByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId() {
        Page<Equipamento> page = new PageImpl<>(Arrays.asList(new Equipamento()));
        when(repository.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class))).thenReturn(page);

        Page<Equipamento> result = service.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId("teste", 1,
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
        Equipamento entity = new Equipamento();
        EquipamentoDTO dto = new EquipamentoDTO();
        when(mapper.toDto(entity)).thenReturn(dto);

        EquipamentoDTO result = service.convertToDto(entity);

        assertEquals(dto, result);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    void testConvertToEntity() {
        EquipamentoDTO dto = new EquipamentoDTO();
        Equipamento entity = new Equipamento();
        when(mapper.toEntity(dto)).thenReturn(entity);

        Equipamento result = service.convertToEntity(dto);

        assertEquals(entity, result);
        verify(mapper, times(1)).toEntity(dto);
    }
}
