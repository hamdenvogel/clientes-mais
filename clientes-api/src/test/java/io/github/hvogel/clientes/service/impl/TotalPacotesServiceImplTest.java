package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.repository.PacoteRepository;

@ExtendWith(MockitoExtension.class)
class TotalPacotesServiceImplTest {

    @InjectMocks
    private TotalPacotesServiceImpl service;

    @Mock
    private PacoteRepository repository;

    @Test
    void testObterTotalPacotes() {
        when(repository.count()).thenReturn(10L);
        long total = service.obterTotalPacotes();
        assertEquals(10L, total);
    }
}
