package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.repository.ProdutosRepository;

@ExtendWith(MockitoExtension.class)
class TotalProdutosServiceImplTest {

    @Mock
    private ProdutosRepository repository;

    @InjectMocks
    private TotalProdutosServiceImpl service;

    @Test
    void testObterTotalProdutos() {
        when(repository.count()).thenReturn(20L);
        assertEquals(20L, service.obterTotalProdutos());
    }
}
