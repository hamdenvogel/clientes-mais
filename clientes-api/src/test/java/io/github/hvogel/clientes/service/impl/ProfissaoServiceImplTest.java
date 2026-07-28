package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import io.github.hvogel.clientes.model.entity.Profissao;
import io.github.hvogel.clientes.model.repository.ProfissaoRepository;

@ExtendWith(MockitoExtension.class)
class ProfissaoServiceImplTest {

    @Mock
    private ProfissaoRepository repository;

    private ProfissaoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProfissaoServiceImpl(repository);
    }

    @Test
    void testObterPorId() {
        Integer id = 1;
        Profissao profissao = new Profissao();
        when(repository.findById(id)).thenReturn(Optional.of(profissao));

        Optional<Profissao> result = service.obterPorId(id);

        assertTrue(result.isPresent());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testObterTodos() {
        Profissao profissao = new Profissao();
        when(repository.findAllByOrderByDescricaoAsc()).thenReturn(Arrays.asList(profissao));

        List<Profissao> result = service.obterTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByOrderByDescricaoAsc();
    }

    @Test
    void testObterPorDescricao() {
        Profissao profissao = new Profissao();
        when(repository.findByDescricaoStartsWith(anyString())).thenReturn(Arrays.asList(profissao));

        List<Profissao> result = service.obterPorDescricao("teste");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByDescricaoStartsWith(anyString());
    }
}
