package io.github.hvogel.clientes.model.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void testGettersAndSetters() {
        ItemPedido item = new ItemPedido();
        Integer id = 1;
        Pedido pedido = new Pedido();
        Produto produto = new Produto();
        Integer qtd = 5;

        item.setId(id);
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(qtd);

        assertEquals(id, item.getId());
        assertEquals(pedido, item.getPedido());
        assertEquals(produto, item.getProduto());
        assertEquals(qtd, item.getQuantidade());
    }

    @Test
    void testEqualsAndHashCode() {
        ItemPedido i1 = new ItemPedido();
        i1.setId(1);
        ItemPedido i2 = new ItemPedido();
        i2.setId(1);

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());

        i2.setId(2);
        assertNotEquals(i1, i2);

        assertNotEquals(null, i1);
        assertNotEquals(i1, new Object());
    }

    @Test
    void testToString() {
        ItemPedido item = new ItemPedido();
        item.setId(1);
        assertNotNull(item.toString());
    }
}
