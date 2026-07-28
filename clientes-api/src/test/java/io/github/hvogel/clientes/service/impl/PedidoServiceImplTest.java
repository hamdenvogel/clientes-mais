package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.enums.StatusPedido;
import io.github.hvogel.clientes.exception.PedidoNaoEncontradoException;
import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.ItemPedido;
import io.github.hvogel.clientes.model.entity.Pedido;
import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.model.repository.ItemsPedidoRepository;
import io.github.hvogel.clientes.model.repository.PedidosRepository;
import io.github.hvogel.clientes.model.repository.ProdutosRepository;
import io.github.hvogel.clientes.model.repository.ServicoPrestadoRepository;
import io.github.hvogel.clientes.rest.dto.ItemPedidoDTO;
import io.github.hvogel.clientes.rest.dto.PedidoDTO;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidosRepository repository;
    @Mock
    private ServicoPrestadoRepository servicoPrestadoRepository;
    @Mock
    private ProdutosRepository produtosRepository;
    @Mock
    private ItemsPedidoRepository itemsPedidoRepository;

    @InjectMocks
    private PedidoServiceImpl service;

    @Test
    void testSalvar_Success() {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);
        dto.setTotal(new BigDecimal("100.00"));

        ItemPedidoDTO itemDto = new ItemPedidoDTO();
        itemDto.setProduto(1);
        itemDto.setQuantidade(2);
        dto.setItems(Arrays.asList(itemDto));

        ServicoPrestado servico = new ServicoPrestado();
        servico.setId(1);

        Produto produto = new Produto();
        produto.setId(1);

        when(servicoPrestadoRepository.findById(1)).thenReturn(Optional.of(servico));
        when(produtosRepository.findById(1)).thenReturn(Optional.of(produto));
        when(repository.save(any(Pedido.class))).thenReturn(new Pedido());
        when(itemsPedidoRepository.saveAll(anyList())).thenReturn(Arrays.asList(new ItemPedido()));

        Pedido result = service.salvar(dto);

        assertNotNull(result);
        verify(repository).save(any(Pedido.class));
        verify(itemsPedidoRepository).saveAll(anyList());
    }

    @Test
    void testSalvar_InvalidServico() {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);

        when(servicoPrestadoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> service.salvar(dto));
    }

    @Test
    void testSalvar_EmptyItems() {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);
        dto.setItems(Collections.emptyList());

        ServicoPrestado servico = new ServicoPrestado();
        when(servicoPrestadoRepository.findById(1)).thenReturn(Optional.of(servico));

        assertThrows(RegraNegocioException.class, () -> service.salvar(dto));
    }

    @Test
    void testSalvar_InvalidProduto() {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);

        ItemPedidoDTO itemDto = new ItemPedidoDTO();
        itemDto.setProduto(1);
        dto.setItems(Arrays.asList(itemDto));

        ServicoPrestado servico = new ServicoPrestado();
        when(servicoPrestadoRepository.findById(1)).thenReturn(Optional.of(servico));
        when(produtosRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> service.salvar(dto));
    }

    @Test
    void testObterPedidoCompleto() {
        when(repository.findByIdFetchItens(1)).thenReturn(Optional.of(new Pedido()));
        Optional<Pedido> result = service.obterPedidoCompleto(1);
        assertTrue(result.isPresent());
    }

    @Test
    void testAtualizaStatus_Success() {
        Pedido pedido = new Pedido();
        pedido.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(pedido));
        when(repository.save(any(Pedido.class))).thenReturn(pedido);

        service.atualizaStatus(1, StatusPedido.CANCELADO);

        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        verify(repository).save(pedido);
    }

    @Test
    void testAtualizaStatus_NotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(PedidoNaoEncontradoException.class, () -> service.atualizaStatus(1, StatusPedido.CANCELADO));
    }

    @Test
    void testFindByIdFetchItensAll() {
        when(repository.findByIdFetchItensAll()).thenReturn(Arrays.asList(new Pedido()));
        List<Pedido> result = service.findByIdFetchItensAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindByIdFetchItensId() {
        when(repository.findByIdFetchItensId(1)).thenReturn(Arrays.asList(new Pedido()));
        List<Pedido> result = service.findByIdFetchItensId(1);
        assertEquals(1, result.size());
    }
}
