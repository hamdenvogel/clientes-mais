package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.repository.ItemPacoteRepository;

@ExtendWith(MockitoExtension.class)
class TotalItensPacotesServiceImplTest {

    @InjectMocks
    private TotalItensPacotesServiceImpl service;

    @Mock
    private ItemPacoteRepository repository;

    @Test
    void testObterTotalItensPacotes() {
        when(repository.countByPacoteId(1)).thenReturn(5L);
        long total = service.obterTotalItensPacotes(1);
        assertEquals(5L, total);
    }
}
