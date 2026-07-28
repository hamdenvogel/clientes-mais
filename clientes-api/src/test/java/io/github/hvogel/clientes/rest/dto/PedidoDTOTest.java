package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class PedidoDTOTest {

    @Test
    void testGettersAndSetters() {
        PedidoDTO dto = new PedidoDTO();

        dto.setServico(1);
        dto.setTotal(new BigDecimal("100.00"));

        List<ItemPedidoDTO> items = new ArrayList<>();
        dto.setItems(items);

        assertEquals(1, dto.getServico());
        assertEquals(new BigDecimal("100.00"), dto.getTotal());
        assertEquals(items, dto.getItems());
    }

    @Test
    void testEquals() {
        PedidoDTO dto1 = new PedidoDTO();
        dto1.setServico(1);
        dto1.setTotal(new BigDecimal("100.00"));

        PedidoDTO dto2 = new PedidoDTO();
        dto2.setServico(1);
        dto2.setTotal(new BigDecimal("100.00"));

        PedidoDTO dto3 = new PedidoDTO();
        dto3.setServico(2);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        PedidoDTO dto1 = new PedidoDTO();
        dto1.setServico(1);

        PedidoDTO dto2 = new PedidoDTO();
        dto2.setServico(1);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PedidoDTO dto = new PedidoDTO();
        dto.setServico(1);

        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("PedidoDTO"));
    }
}
