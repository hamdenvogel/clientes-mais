package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.hvogel.clientes.enums.StatusPedido;
import io.github.hvogel.clientes.model.entity.ItemPedido;
import io.github.hvogel.clientes.model.entity.Pedido;
import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.hvogel.clientes.rest.dto.ItemPedidoDTO;
import io.github.hvogel.clientes.rest.dto.PedidoDTO;
import io.github.hvogel.clientes.service.PedidoService;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoService service;

    @MockBean
    private io.github.hvogel.clientes.security.jwt.JwtUtils jwtUtils;

    @MockBean
    private io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl userDetailsService;

    @MockBean
    private io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil reqUtil;

    @Test
    void testSave() throws Exception {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);
        dto.setTotal(new BigDecimal("100.00"));
        dto.setItems(Collections.singletonList(new ItemPedidoDTO()));

        Pedido pedido = new Pedido();
        pedido.setId(1);

        when(service.salvar(any(PedidoDTO.class))).thenReturn(pedido);

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void testGetById() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setDataPedido(LocalDate.now());
        pedido.setTotal(new BigDecimal("100.00"));
        pedido.setStatus(StatusPedido.REALIZADO);

        ItemPedido item = new ItemPedido();
        item.setQuantidade(1);
        Produto produto = new Produto();
        produto.setDescricao("Produto Teste");
        produto.setPreco(new BigDecimal("50.00"));
        item.setProduto(produto);
        pedido.setItens(Collections.singletonList(item));

        when(service.obterPedidoCompleto(1)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(1))
                .andExpect(jsonPath("$.total").value(100.00));
    }

    @Test
    void testGetById_EmptyItems() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setDataPedido(LocalDate.now());
        pedido.setTotal(new BigDecimal("100.00"));
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setItens(Collections.emptyList());

        when(service.obterPedidoCompleto(1)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(service.obterPedidoCompleto(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateStatus() throws Exception {
        AtualizacaoStatusPedidoDTO dto = new AtualizacaoStatusPedidoDTO();
        dto.setNovoStatus("CANCELADO");

        doNothing().when(service).atualizaStatus(anyInt(), any(StatusPedido.class));

        mockMvc.perform(patch("/api/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindByIdFetchItensAll() throws Exception {
        when(service.findByIdFetchItensAll()).thenReturn(Arrays.asList(new Pedido()));

        mockMvc.perform(get("/api/pedidos/todos"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByIdFetchItensId() throws Exception {
        when(service.findByIdFetchItensId(1)).thenReturn(Arrays.asList(new Pedido()));

        mockMvc.perform(get("/api/pedidos/todos/1"))
                .andExpect(status().isOk());
    }
}
