package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.enums.SearchOperation;
import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.model.repository.ProdutosRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    private ProdutosRepository repository;

    @InjectMocks
    private ProdutoServiceImpl service;

    @Test
    void testSalvar_Success() {
        Produto produto = new Produto();
        produto.setDescricao("produto teste");

        when(repository.findByDescricao(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto result = service.salvar(produto);

        assertNotNull(result);
        assertEquals("Produto teste", produto.getDescricao()); // Capitalization check
        verify(repository).save(produto);
    }

    @Test
    void testSalvar_DuplicateDescricao() {
        Produto produto = new Produto();
        produto.setDescricao("Produto Teste");

        when(repository.findByDescricao(anyString())).thenReturn(Optional.of(new Produto()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(produto));
    }

    @Test
    void testAtualizar_Success() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setDescricao("produto teste");

        when(repository.findByDescricaoAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto result = service.atualizar(produto);

        assertNotNull(result);
        assertEquals("Produto teste", produto.getDescricao());
        verify(repository).save(produto);
    }

    @Test
    void testAtualizar_DuplicateDescricao() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setDescricao("Produto Teste");

        when(repository.findByDescricaoAndIdNot(anyString(), anyInt())).thenReturn(Optional.of(new Produto()));

        assertThrows(ResponseStatusException.class, () -> service.atualizar(produto));
    }

    @Test
    void testDeletar() {
        Produto produto = new Produto();
        service.deletar(produto);
        verify(repository).delete(produto);
    }

    @Test
    void testObterPorId() {
        Produto produto = new Produto();
        produto.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(produto));

        Optional<Produto> result = service.obterPorId(1);
        assertTrue(result.isPresent());
    }

    @Test
    void testObterPorIdPageable() {
        Page<Produto> page = new PageImpl<>(Arrays.asList(new Produto()));
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findByIdProduto(1, pageable)).thenReturn(page);

        Page<Produto> result = service.obterPorId(1, pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testObterTodos() {
        when(repository.findAllByOrderByDescricaoAsc()).thenReturn(Arrays.asList(new Produto()));
        List<Produto> result = service.obterTodos();
        assertEquals(1, result.size());
    }

    @Test
    void testRecuperarTodos() {
        Page<Produto> page = new PageImpl<>(Arrays.asList(new Produto()));
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(page);

        Page<Produto> result = service.recuperarTodos(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testPesquisarPelaDescricao() {
        Page<Produto> page = new PageImpl<>(Arrays.asList(new Produto()));
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findByDescricaoContainsAllIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Produto> result = service.pesquisarPelaDescricao("Produto", pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testExecutaCriteria() {
        Page<Produto> page = new PageImpl<>(Arrays.asList(new Produto()));
        Pageable pageable = PageRequest.of(0, 10);
        SearchCriteria criteria = new SearchCriteria("descricao", "Produto", SearchOperation.LIKE);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Produto> result = service.executaCriteria(Arrays.asList(criteria), pageable);
        assertEquals(1, result.getTotalElements());
    }
}
