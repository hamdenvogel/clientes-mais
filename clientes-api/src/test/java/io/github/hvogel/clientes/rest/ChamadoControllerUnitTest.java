package io.github.hvogel.clientes.rest;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Chamado;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.mapper.ChamadoMapper;
import io.github.hvogel.clientes.rest.dto.ChamadoDTO;
import io.github.hvogel.clientes.service.ChamadoService;
import io.github.hvogel.clientes.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ChamadoControllerUnitTest {

    @InjectMocks
    private ChamadoController controller;

    @Mock
    private ChamadoService chamadoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ChamadoMapper chamadoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvar_Success() {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);

        when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
        Chamado entity = new Chamado();
        when(chamadoMapper.toEntity(dto)).thenReturn(entity);
        when(chamadoService.salvar(entity)).thenReturn(entity);
        when(chamadoMapper.toDto(entity)).thenReturn(dto);

        ChamadoDTO result = controller.salvar(dto);
        assertNotNull(result);
    }

    @Test
    void testSalvar_ClienteNotFound() {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);
        when(clienteService.obterPorId(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> controller.salvar(dto));
    }

    @Test
    void testSalvar_Exception() {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);

        when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
        Chamado entity = new Chamado();
        when(chamadoMapper.toEntity(dto)).thenReturn(entity);
        when(chamadoService.salvar(entity)).thenThrow(new RegraNegocioException("Erro"));

        assertThrows(ResponseStatusException.class, () -> controller.salvar(dto));
    }

    @Test
    void testAtualizar_Success() {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);

        when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
        when(chamadoService.findOneById(1L)).thenReturn(Optional.of(new Chamado()));

        Chamado entity = new Chamado();
        when(chamadoMapper.toEntity(dto)).thenReturn(entity);
        when(chamadoService.salvar(entity)).thenReturn(entity);
        when(chamadoMapper.toDto(entity)).thenReturn(dto);

        ChamadoDTO result = controller.atualizar(1L, dto);
        assertNotNull(result);
    }

    @Test
    void testAtualizar_ClienteNotFound() {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);
        when(clienteService.obterPorId(1)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> controller.atualizar(1L, dto));
    }

    @Test
    void testAtualizar_ChamadoNotFound() {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);
        when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
        when(chamadoService.findOneById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> controller.atualizar(1L, dto));
    }

    @Test
    void testAtualizar_Exception() {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);
        when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
        when(chamadoService.findOneById(1L)).thenReturn(Optional.of(new Chamado()));

        Chamado entity = new Chamado();
        when(chamadoMapper.toEntity(dto)).thenReturn(entity);
        when(chamadoService.salvar(entity)).thenThrow(new RegraNegocioException("Erro"));

        assertThrows(ResponseStatusException.class, () -> controller.atualizar(1L, dto));
    }

    @Test
    void testDeletar_Success() {
        when(chamadoService.findOneById(1L)).thenReturn(Optional.of(new Chamado()));
        assertDoesNotThrow(() -> controller.deletar(1L));
        verify(chamadoService).deleteById(1L);
    }

    @Test
    void testDeletar_NotFound() {
        when(chamadoService.findOneById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> controller.deletar(1L));
    }

    @Test
    void testDeletar_Exception() {
        when(chamadoService.findOneById(1L)).thenReturn(Optional.of(new Chamado()));
        doThrow(new RegraNegocioException("Erro")).when(chamadoService).deleteById(1L);
        assertThrows(ResponseStatusException.class, () -> controller.deletar(1L));
    }
}
