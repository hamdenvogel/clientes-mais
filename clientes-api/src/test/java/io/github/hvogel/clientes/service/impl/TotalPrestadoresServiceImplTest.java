package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.repository.PrestadorRepository;

@ExtendWith(MockitoExtension.class)
class TotalPrestadoresServiceImplTest {

    @Mock
    private PrestadorRepository repository;

    @InjectMocks
    private TotalPrestadoresServiceImpl service;

    @Test
    void testObterTotalPrestadores() {
        when(repository.count()).thenReturn(15L);
        assertEquals(15L, service.obterTotalPrestadores());
    }
}
