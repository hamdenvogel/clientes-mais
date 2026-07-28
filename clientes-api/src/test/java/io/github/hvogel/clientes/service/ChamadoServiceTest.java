package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.entity.Chamado;
import io.github.hvogel.clientes.model.mapper.ChamadoMapper;
import io.github.hvogel.clientes.model.repository.ChamadoRepository;
import io.github.hvogel.clientes.rest.dto.ChamadoDTO;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository repository;

    @Mock
    private ChamadoMapper mapper;

    private ChamadoService service;

    @BeforeEach
    void setUp() {
        // Passing repository twice as per the current constructor definition
        service = new ChamadoService(repository, mapper, repository);
    }

    @Test
    void testObterPorId() {
        Chamado chamado = new Chamado();
        chamado.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(chamado));

        Optional<Chamado> result = service.obterPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testSalvar() {
        Chamado chamado = new Chamado();
        when(repository.save(any(Chamado.class))).thenReturn(chamado);

        Chamado result = service.salvar(chamado);

        assertNotNull(result);
        verify(repository, times(1)).save(chamado);
    }

    @Test
    void testConvertToDto() {
        Chamado entity = new Chamado();
        ChamadoDTO dto = new ChamadoDTO();
        when(mapper.toDto(entity)).thenReturn(dto);

        ChamadoDTO result = service.convertToDto(entity);

        assertEquals(dto, result);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    void testConvertToEntity() {
        ChamadoDTO dto = new ChamadoDTO();
        Chamado entity = new Chamado();
        when(mapper.toEntity(dto)).thenReturn(entity);

        Chamado result = service.convertToEntity(dto);

        assertEquals(entity, result);
        verify(mapper, times(1)).toEntity(dto);
    }
}
