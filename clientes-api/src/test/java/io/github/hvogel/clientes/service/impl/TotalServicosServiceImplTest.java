package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.repository.ServicoPrestadoRepository;

@ExtendWith(MockitoExtension.class)
class TotalServicosServiceImplTest {

    @Mock
    private ServicoPrestadoRepository repository;

    @InjectMocks
    private TotalServicosServiceImpl service;

    @Test
    void testObterTotalServicos() {
        when(repository.count()).thenReturn(25L);
        assertEquals(25L, service.obterTotalServicos());
    }
}
