package io.github.hvogel.clientes.rest;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Solucao;
import io.github.hvogel.clientes.rest.dto.SolucaoDTO;
import io.github.hvogel.clientes.rest.dto.TotalRegistrosDTO;
import io.github.hvogel.clientes.service.SolucaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

class SolucaoControllerUnitTest {

    @InjectMocks
    private SolucaoController controller;

    @Mock
    private SolucaoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObterListaSemPaginacao() {
        when(service.obterSolucoes()).thenReturn(Collections.singletonList(new Solucao()));
        List<Solucao> result = controller.obterListaSemPaginacao();
        assertFalse(result.isEmpty());
    }

    @Test
    void testObterPorId_Success() {
        when(service.obterPorId(1L)).thenReturn(Optional.of(new Solucao()));
        Solucao result = controller.obterPorId(1L);
        assertNotNull(result);
    }

    @Test
    void testObterPorId_NotFound() {
        when(service.obterPorId(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> controller.obterPorId(1L));
    }

    @Test
    void testAtualizarDescricao_Success() {
        Solucao solucao = new Solucao();
        SolucaoDTO dto = new SolucaoDTO();
        dto.setDescricao("Nova Descricao");

        when(service.obterPorId(1L)).thenReturn(Optional.of(solucao));
        when(service.save(any(Solucao.class))).thenReturn(solucao);

        Solucao result = controller.atualizarDescricao(1L, dto);
        assertNotNull(result);
        assertEquals("Nova Descricao", solucao.getDescricao());
    }

    @Test
    void testAtualizarDescricao_NotFound() {
        SolucaoDTO dto = new SolucaoDTO();
        when(service.obterPorId(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> controller.atualizarDescricao(1L, dto));
    }

    @Test
    void testAtualizarDescricao_Exception() {
        SolucaoDTO dto = new SolucaoDTO();
        dto.setDescricao("Desc");
        when(service.obterPorId(1L)).thenReturn(Optional.of(new Solucao()));
        when(service.save(any(Solucao.class))).thenThrow(new RegraNegocioException("Error"));

        assertThrows(ResponseStatusException.class, () -> controller.atualizarDescricao(1L, dto));
    }

    @Test
    void testTotal() {
        when(service.count()).thenReturn(10L);
        TotalRegistrosDTO result = controller.total();
        assertEquals(10L, result.getTotal());
    }

    @Test
    void testList_Defaultsort() {
        when(service.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Page<Solucao> result = controller.list(0, 10, new String[] { "descricao", "asc" }, null, null);
        assertNotNull(result);
    }

    @Test
    void testList_ComplexSort() {
        when(service.findAll(any(Pageable.class))).thenReturn(Page.empty());
        String[] sort = { "descricao,desc", "id,asc" };
        Page<Solucao> result = controller.list(0, 10, sort, null, null);
        assertNotNull(result);
    }

    @Test
    void testList_FilterDescricaoAndServico() {
        when(service.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class)))
                .thenReturn(Page.empty());
        Page<Solucao> result = controller.list(0, 10, new String[] { "descricao,asc" }, "desc", 1);
        assertNotNull(result);
        verify(service).findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class));
    }

    @Test
    void testList_FilterOnlyServico() {
        when(service.findByServicoPrestadoId(anyInt(), any(Pageable.class))).thenReturn(Page.empty());
        Page<Solucao> result = controller.list(0, 10, new String[] { "descricao,asc" }, null, 1);
        assertNotNull(result);
        verify(service).findByServicoPrestadoId(anyInt(), any(Pageable.class));
    }
}
