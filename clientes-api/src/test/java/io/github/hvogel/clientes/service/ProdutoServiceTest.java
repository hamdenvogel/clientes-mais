package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.model.repository.ProdutosRepository;
import io.github.hvogel.clientes.service.impl.ProdutoServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutosRepository repository;

    private ProdutoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProdutoServiceImpl(repository);
    }

    @Test
    void testSalvar_Success() {
        Produto produto = new Produto();
        produto.setDescricao("Produto teste");
        produto.setPreco(new java.math.BigDecimal("10.00"));

        when(repository.findByDescricao(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto result = service.salvar(produto);

        assertNotNull(result);
        assertEquals("Produto teste", produto.getDescricao());
        verify(repository, times(1)).save(produto);
    }

    @Test
    void testSalvar_DuplicateDescricao() {
        Produto produto = new Produto();
        produto.setDescricao("Produto teste");

        when(repository.findByDescricao(anyString())).thenReturn(Optional.of(new Produto()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(produto));
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    void testAtualizar_Success() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setDescricao("Produto teste");

        when(repository.findByDescricaoAndIdNot(anyString(), any())).thenReturn(Optional.empty());
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto result = service.atualizar(produto);

        assertNotNull(result);
        verify(repository, times(1)).save(produto);
    }
}
