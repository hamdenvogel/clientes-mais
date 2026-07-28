package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import io.github.hvogel.clientes.model.entity.Atividade;
import io.github.hvogel.clientes.model.mapper.AtividadeMapper;
import io.github.hvogel.clientes.model.repository.AtividadeRepository;
import io.github.hvogel.clientes.rest.dto.AtividadeDTO;

@ExtendWith(MockitoExtension.class)
class AtividadeServiceTest {

    @Mock
    private AtividadeRepository repository;

    @Mock
    private AtividadeMapper mapper;

    private AtividadeService service;

    @BeforeEach
    void setUp() {
        service = new AtividadeService(repository, mapper);
    }

    @Test
    void testObterAtividades() {
        Atividade atividade = new Atividade();
        atividade.setId(1L);
        when(repository.findAllByOrderByDescricaoAsc()).thenReturn(Arrays.asList(atividade));

        List<Atividade> result = service.obterAtividades();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByOrderByDescricaoAsc();
    }

    @Test
    void testObterPorId() {
        Atividade atividade = new Atividade();
        atividade.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(atividade));

        Optional<Atividade> result = service.obterPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testConvertToDto() {
        Atividade entity = new Atividade();
        AtividadeDTO dto = new AtividadeDTO();
        when(mapper.toDto(entity)).thenReturn(dto);

        AtividadeDTO result = service.convertToDto(entity);

        assertEquals(dto, result);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    void testConvertToEntity() {
        AtividadeDTO dto = new AtividadeDTO();
        Atividade entity = new Atividade();
        when(mapper.toEntity(dto)).thenReturn(entity);

        Atividade result = service.convertToEntity(dto);

        assertEquals(entity, result);
        verify(mapper, times(1)).toEntity(dto);
    }

    @Test
    void testSave() {
        Atividade entity = new Atividade();
        when(repository.save(any(Atividade.class))).thenReturn(entity);

        Atividade result = service.save(entity);

        assertNotNull(result);
        verify(repository, times(1)).save(entity);
    }
}
