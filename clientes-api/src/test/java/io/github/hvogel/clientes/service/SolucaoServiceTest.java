package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.model.entity.Solucao;
import io.github.hvogel.clientes.model.mapper.SolucaoMapper;
import io.github.hvogel.clientes.model.repository.SolucaoRepository;
import io.github.hvogel.clientes.rest.dto.SolucaoDTO;

@ExtendWith(MockitoExtension.class)
class SolucaoServiceTest {

    @Mock
    private SolucaoRepository repository;

    @Mock
    private SolucaoMapper mapper;

    @InjectMocks
    private SolucaoService service;

    @Test
    void testConvertToDto() {
        Solucao entity = new Solucao();
        entity.setId(1L);
        entity.setDescricao("Test");

        SolucaoDTO dto = new SolucaoDTO();
        dto.setId(1L);
        dto.setDescricao("Test");

        when(mapper.toDto(any(Solucao.class))).thenReturn(dto);

        SolucaoDTO result = service.convertToDto(entity);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testConvertToEntity() {
        SolucaoDTO dto = new SolucaoDTO();
        dto.setId(1L);
        dto.setDescricao("Test");

        Solucao entity = new Solucao();
        entity.setId(1L);
        entity.setDescricao("Test");

        when(mapper.toEntity(any(SolucaoDTO.class))).thenReturn(entity);

        Solucao result = service.convertToEntity(dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testObterSolucoes() {
        Solucao s1 = new Solucao();
        s1.setId(1L);
        Solucao s2 = new Solucao();
        s2.setId(2L);

        when(repository.findAllByOrderByDescricaoAsc()).thenReturn(Arrays.asList(s1, s2));

        List<Solucao> result = service.obterSolucoes();
        assertEquals(2, result.size());
    }

    @Test
    void testObterPorId() {
        Solucao entity = new Solucao();
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Solucao> result = service.obterPorId(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testFindByServicoPrestadoId() {
        Solucao s1 = new Solucao();
        Page<Solucao> page = new PageImpl<>(Arrays.asList(s1));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByServicoPrestadoId(anyInt(), any(Pageable.class))).thenReturn(page);

        Page<Solucao> result = service.findByServicoPrestadoId(1, pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindByDescricaoContains() {
        Solucao s1 = new Solucao();
        Page<Solucao> page = new PageImpl<>(Arrays.asList(s1));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class)))
                .thenReturn(page);

        Page<Solucao> result = service.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId("test", 1, pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testCount() {
        when(repository.count()).thenReturn(5L);
        assertEquals(5L, service.count());
    }
}
