package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class TotalUsuariosServiceImplTest {

    @InjectMocks
    private TotalUsuariosServiceImpl service;

    @Mock
    private UsuarioRepository repository;

    @Test
    void testObterTotalUsuarios() {
        when(repository.count()).thenReturn(20L);
        long total = service.obterTotalUsuarios();
        assertEquals(20L, total);
    }
}
