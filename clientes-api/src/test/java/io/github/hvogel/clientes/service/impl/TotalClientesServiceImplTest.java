package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class TotalClientesServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private TotalClientesServiceImpl service;

    @Test
    void testObterTotalClientes() {
        when(repository.count()).thenReturn(10L);
        assertEquals(10L, service.obterTotalClientes());
    }
}
