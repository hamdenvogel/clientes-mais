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

import io.github.hvogel.clientes.model.entity.Documento;
import io.github.hvogel.clientes.model.repository.DocumentoRepository;
import io.github.hvogel.clientes.service.impl.DocumentoServiceImpl;

@ExtendWith(MockitoExtension.class)
class DocumentoServiceTest {

    @Mock
    private DocumentoRepository repository;

    private DocumentoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DocumentoServiceImpl(repository);
    }

    @Test
    void testObterPorId() {
        Documento documento = new Documento();
        documento.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(documento));

        Optional<Documento> result = service.obterPorId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testObterPorIdPageable() {
        Page<Documento> page = new PageImpl<>(Arrays.asList(new Documento()));
        when(repository.findByIdDocumento(anyInt(), any(Pageable.class))).thenReturn(page);

        Page<Documento> result = service.obterPorId(1, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByIdDocumento(anyInt(), any(Pageable.class));
    }

    @Test
    void testObterTodos() {
        Documento documento = new Documento();
        when(repository.findAll()).thenReturn(Arrays.asList(documento));

        List<Documento> result = service.obterTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testPesquisarPelaDescricao() {
        Page<Documento> page = new PageImpl<>(Arrays.asList(new Documento()));
        when(repository.findByDescricaoContainsAllIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Documento> result = service.pesquisarPelaDescricao("teste", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByDescricaoContainsAllIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void testObterTodosPageable() {
        Page<Documento> page = new PageImpl<>(Arrays.asList(new Documento()));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Documento> result = service.obterTodos(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }
}
