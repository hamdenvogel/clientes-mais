package io.github.hvogel.clientes.service;

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
import io.github.hvogel.clientes.service.impl.ProfissaoServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProfissaoServiceTest {

    @Mock
    private ProfissaoRepository repository;

    private ProfissaoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProfissaoServiceImpl(repository);
    }

    @Test
    void testObterPorId() {
        Profissao profissao = new Profissao();
        profissao.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(profissao));

        Optional<Profissao> result = service.obterPorId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(repository, times(1)).findById(1);
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
        verify(repository, times(1)).findByDescricaoStartsWith("teste");
    }
}
