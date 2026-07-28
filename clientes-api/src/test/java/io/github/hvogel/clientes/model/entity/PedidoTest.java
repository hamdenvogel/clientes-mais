package io.github.hvogel.clientes.model.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import io.github.hvogel.clientes.enums.StatusPedido;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void testGettersAndSetters() {
        Pedido pedido = new Pedido();
        Integer id = 1;
        ServicoPrestado servico = new ServicoPrestado();
        LocalDate data = LocalDate.now();
        BigDecimal total = BigDecimal.TEN;
        StatusPedido status = StatusPedido.REALIZADO;
        List<ItemPedido> itens = new ArrayList<>();

        pedido.setId(id);
        pedido.setServicoPrestado(servico);
        pedido.setDataPedido(data);
        pedido.setTotal(total);
        pedido.setStatus(status);
        pedido.setItens(itens);

        assertEquals(id, pedido.getId());
        assertEquals(servico, pedido.getServicoPrestado());
        assertEquals(data, pedido.getDataPedido());
        assertEquals(total, pedido.getTotal());
        assertEquals(status, pedido.getStatus());
        assertEquals(itens, pedido.getItens());
    }

    @Test
    void testEqualsAndHashCode() {
        Pedido p1 = new Pedido();
        p1.setId(1);
        Pedido p2 = new Pedido();
        p2.setId(1);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        p2.setId(2);
        assertNotEquals(p1, p2);
        assertNotEquals(p1.hashCode(), p2.hashCode());

        assertNotEquals(null, p1);
        assertNotEquals(p1, new Object());
    }

    @Test
    void testToString() {
        Pedido pedido = new Pedido();
        pedido.setId(1);
        assertNotNull(pedido.toString());
    }
}
