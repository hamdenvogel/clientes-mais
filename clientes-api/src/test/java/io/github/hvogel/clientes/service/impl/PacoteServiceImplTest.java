package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.model.entity.Pacote;
import io.github.hvogel.clientes.model.repository.PacoteRepository;
import io.github.hvogel.clientes.service.ValidadorService;

@ExtendWith(MockitoExtension.class)
class PacoteServiceImplTest {

    @Mock
    private PacoteRepository repository;

    @Mock
    private ValidadorService validadorService;

    private PacoteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PacoteServiceImpl(repository, validadorService);
    }

    @Test
    void testSalvar() {
        Pacote pacote = new Pacote();
        pacote.setDescricao("Pacote Teste");
        when(repository.findByDescricao(anyString())).thenReturn(Optional.empty());
        when(repository.save(pacote)).thenReturn(pacote);
        doNothing().when(validadorService).validarTipoPacote(any());

        Pacote result = service.salvar(pacote);

        assertNotNull(result);
        verify(repository, times(1)).save(pacote);
    }

    @Test
    void testSalvar_Duplicado() {
        Pacote pacote = new Pacote();
        pacote.setDescricao("Pacote Teste");
        when(repository.findByDescricao(anyString())).thenReturn(Optional.of(pacote));

        assertThrows(ResponseStatusException.class, () -> service.salvar(pacote));
    }

    @Test
    void testAtualizar() {
        Pacote pacote = new Pacote();
        pacote.setId(1);
        pacote.setDescricao("Pacote Teste");
        when(repository.findByDescricaoAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.save(pacote)).thenReturn(pacote);
        doNothing().when(validadorService).validarTipoPacote(any());

        Pacote result = service.atualizar(pacote);

        assertNotNull(result);
        verify(repository, times(1)).save(pacote);
    }

    @Test
    void testAtualizar_Duplicado() {
        Pacote pacote = new Pacote();
        pacote.setId(1);
        pacote.setDescricao("Pacote Teste");
        when(repository.findByDescricaoAndIdNot(anyString(), anyInt())).thenReturn(Optional.of(new Pacote()));

        assertThrows(ResponseStatusException.class, () -> service.atualizar(pacote));
    }

    @Test
    void testDeletar() {
        Pacote pacote = new Pacote();
        service.deletar(pacote);
        verify(repository, times(1)).delete(pacote);
    }

    @Test
    void testObterPorId() {
        Integer id = 1;
        Pacote pacote = new Pacote();
        when(repository.findById(id)).thenReturn(Optional.of(pacote));

        Optional<Pacote> result = service.obterPorId(id);

        assertTrue(result.isPresent());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testObterPorIdPageable() {
        Page<Pacote> page = new PageImpl<>(Arrays.asList(new Pacote()));
        when(repository.findByIdPacote(anyInt(), any(Pageable.class))).thenReturn(page);

        Page<Pacote> result = service.obterPorId(1, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByIdPacote(anyInt(), any(Pageable.class));
    }

    @Test
    void testObterTodos() {
        Pacote pacote = new Pacote();
        when(repository.findAllByOrderByDescricaoAsc()).thenReturn(Arrays.asList(pacote));

        List<Pacote> result = service.obterTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByOrderByDescricaoAsc();
    }

    @Test
    void testRecuperarTodos() {
        Page<Pacote> page = new PageImpl<>(Arrays.asList(new Pacote()));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Pacote> result = service.recuperarTodos(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testPercentuais() {
        when(repository.iPercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));
        when(repository.aPercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));
        when(repository.ePercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));
        when(repository.cPercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));
        when(repository.fPercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));

        assertEquals(1, service.iPercentual().size());
        assertEquals(1, service.aPercentual().size());
        assertEquals(1, service.ePercentual().size());
        assertEquals(1, service.cPercentual().size());
        assertEquals(1, service.fPercentual().size());
    }

    @Test
    void testPesquisarPelaDescricao() {
        Page<Pacote> page = new PageImpl<>(Arrays.asList(new Pacote()));
        when(repository.findByDescricaoContainsAllIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Pacote> result = service.pesquisarPelaDescricao("teste", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByDescricaoContainsAllIgnoreCase(anyString(), any(Pageable.class));
    }
}
