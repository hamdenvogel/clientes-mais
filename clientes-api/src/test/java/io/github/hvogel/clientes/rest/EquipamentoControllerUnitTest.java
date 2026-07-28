package io.github.hvogel.clientes.rest;

import io.github.hvogel.clientes.model.entity.Equipamento;
import io.github.hvogel.clientes.rest.dto.TotalRegistrosDTO;
import io.github.hvogel.clientes.service.EquipamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EquipamentoControllerUnitTest {

    @InjectMocks
    private EquipamentoController controller;

    @Mock
    private EquipamentoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObterListaSemPaginacao() {
        when(service.obterEquipamentos()).thenReturn(Collections.singletonList(new Equipamento()));
        List<Equipamento> result = controller.obterListaSemPaginacao();
        assertFalse(result.isEmpty());
    }

    @Test
    void testObterPorId_Success() {
        when(service.obterPorId(1L)).thenReturn(Optional.of(new Equipamento()));
        Equipamento result = controller.obterPorId(1L);
        assertNotNull(result);
    }

    @Test
    void testObterPorId_NotFound() {
        when(service.obterPorId(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> controller.obterPorId(1L));
    }

    @Test
    void testTotal() {
        when(service.count()).thenReturn(5L);
        TotalRegistrosDTO result = controller.total();
        assertEquals(5L, result.getTotal());
    }

    @Test
    void testList_DefaultSort() {
        when(service.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Page<Equipamento> result = controller.list(0, 10, new String[] { "descricao,asc" }, null, null);
        assertNotNull(result);
    }

    @Test
    void testList_ComplexSort() {
        when(service.findAll(any(Pageable.class))).thenReturn(Page.empty());
        String[] sort = { "descricao,desc", "id,asc" };
        Page<Equipamento> result = controller.list(0, 10, sort, null, null);
        assertNotNull(result);
    }

    @Test
    void testList_FilterDescricaoAndServico() {
        when(service.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class)))
                .thenReturn(Page.empty());
        Page<Equipamento> result = controller.list(0, 10, new String[] { "descricao,asc" }, "desc", 1);
        assertNotNull(result);
        verify(service).findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class));
    }

    @Test
    void testList_FilterOnlyServico() {
        when(service.findByServicoPrestadoId(anyInt(), any(Pageable.class))).thenReturn(Page.empty());
        Page<Equipamento> result = controller.list(0, 10, new String[] { "descricao,asc" }, null, 1);
        assertNotNull(result);
        verify(service).findByServicoPrestadoId(anyInt(), any(Pageable.class));
    }
}
