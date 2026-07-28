package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.enums.StatusPedido;
import io.github.hvogel.clientes.exception.PedidoNaoEncontradoException;
import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Pedido;
import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.model.repository.ItemsPedidoRepository;
import io.github.hvogel.clientes.model.repository.PedidosRepository;
import io.github.hvogel.clientes.model.repository.ProdutosRepository;
import io.github.hvogel.clientes.model.repository.ServicoPrestadoRepository;
import io.github.hvogel.clientes.rest.dto.ItemPedidoDTO;
import io.github.hvogel.clientes.rest.dto.PedidoDTO;
import io.github.hvogel.clientes.service.impl.PedidoServiceImpl;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidosRepository repository;
    @Mock
    private ServicoPrestadoRepository servicoPrestadoRepository;
    @Mock
    private ProdutosRepository produtosRepository;
    @Mock
    private ItemsPedidoRepository itemsPedidoRepository;

    private PedidoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PedidoServiceImpl(repository, servicoPrestadoRepository, produtosRepository,
                itemsPedidoRepository);
    }

    @Test
    void testSalvar_Success() {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);
        dto.setTotal(BigDecimal.TEN);
        ItemPedidoDTO itemDTO = new ItemPedidoDTO();
        itemDTO.setProduto(1);
        itemDTO.setQuantidade(1);
        dto.setItems(Arrays.asList(itemDTO));

        ServicoPrestado servico = new ServicoPrestado();
        servico.setId(1);
        Produto produto = new Produto();
        produto.setId(1);

        when(servicoPrestadoRepository.findById(1)).thenReturn(Optional.of(servico));
        when(produtosRepository.findById(1)).thenReturn(Optional.of(produto));
        when(repository.save(any(Pedido.class))).thenReturn(new Pedido());
        when(itemsPedidoRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        Pedido result = service.salvar(dto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Pedido.class));
        verify(itemsPedidoRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSalvar_ServicoInvalido() {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);

        when(servicoPrestadoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> service.salvar(dto));
    }

    @Test
    void testSalvar_SemItens() {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);
        dto.setItems(Collections.emptyList());

        ServicoPrestado servico = new ServicoPrestado();
        when(servicoPrestadoRepository.findById(1)).thenReturn(Optional.of(servico));

        assertThrows(RegraNegocioException.class, () -> service.salvar(dto));
    }

    @Test
    void testObterPedidoCompleto() {
        Pedido pedido = new Pedido();
        when(repository.findByIdFetchItens(anyInt())).thenReturn(Optional.of(pedido));

        Optional<Pedido> result = service.obterPedidoCompleto(1);

        assertTrue(result.isPresent());
        verify(repository, times(1)).findByIdFetchItens(1);
    }

    @Test
    void testAtualizaStatus_Success() {
        Pedido pedido = new Pedido();
        when(repository.findById(1)).thenReturn(Optional.of(pedido));
        when(repository.save(any(Pedido.class))).thenReturn(pedido);

        service.atualizaStatus(1, StatusPedido.CANCELADO);

        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        verify(repository, times(1)).save(pedido);
    }

    @Test
    void testAtualizaStatus_NaoEncontrado() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class, () -> service.atualizaStatus(1, StatusPedido.CANCELADO));
    }
}
