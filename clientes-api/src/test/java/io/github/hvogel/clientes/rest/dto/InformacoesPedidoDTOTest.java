package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class InformacoesPedidoDTOTest {

    @Test
    void testBuilder() {
        // Arrange
        Integer codigo = 1;
        BigDecimal total = new BigDecimal("100.50");
        String dataPedido = "2025-12-13";
        String status = "PENDENTE";
        List<InformacaoItemPedidoDTO> items = new ArrayList<>();

        // Act
        InformacoesPedidoDTO dto = InformacoesPedidoDTO.builder()
                .withCodigo(codigo)
                .withTotal(total)
                .withDataPedido(dataPedido)
                .withStatus(status)
                .withItems(items)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(codigo, dto.getCodigo());
        assertEquals(total, dto.getTotal());
        assertEquals(dataPedido, dto.getDataPedido());
        assertEquals(status, dto.getStatus());
        assertEquals(items, dto.getItems());
    }

    @Test
    void testBuilderWithEmptyList() {
        // Act
        InformacoesPedidoDTO dto = InformacoesPedidoDTO.builder().build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getCodigo());
        assertNull(dto.getTotal());
        assertNull(dto.getDataPedido());
        assertNull(dto.getStatus());
        assertEquals(Collections.emptyList(), dto.getItems());
    }

    @Test
    void testSetters() {
        // Arrange
        InformacoesPedidoDTO dto = InformacoesPedidoDTO.builder().build();
        Integer codigo = 2;
        BigDecimal total = new BigDecimal("200.75");
        String dataPedido = "2025-12-14";
        String status = "APROVADO";
        List<InformacaoItemPedidoDTO> items = new ArrayList<>();

        // Act
        dto.setCodigo(codigo);
        dto.setTotal(total);
        dto.setDataPedido(dataPedido);
        dto.setStatus(status);
        dto.setItems(items);

        // Assert
        assertEquals(codigo, dto.getCodigo());
        assertEquals(total, dto.getTotal());
        assertEquals(dataPedido, dto.getDataPedido());
        assertEquals(status, dto.getStatus());
        assertEquals(items, dto.getItems());
    }

    @Test
    void testEquals() {
        // Arrange
        InformacoesPedidoDTO dto1 = InformacoesPedidoDTO.builder()
                .withCodigo(1)
                .withTotal(new BigDecimal("100.50"))
                .withDataPedido("2025-12-13")
                .withStatus("PENDENTE")
                .withItems(Collections.emptyList())
                .build();

        InformacoesPedidoDTO dto2 = InformacoesPedidoDTO.builder()
                .withCodigo(1)
                .withTotal(new BigDecimal("100.50"))
                .withDataPedido("2025-12-13")
                .withStatus("PENDENTE")
                .withItems(Collections.emptyList())
                .build();

        InformacoesPedidoDTO dto3 = InformacoesPedidoDTO.builder()
                .withCodigo(2)
                .withTotal(new BigDecimal("200.75"))
                .withDataPedido("2025-12-14")
                .withStatus("APROVADO")
                .withItems(Collections.emptyList())
                .build();

        // Assert
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1, dto1);
        assertNotEquals(null, dto1);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void testHashCode() {
        // Arrange
        InformacoesPedidoDTO dto1 = InformacoesPedidoDTO.builder()
                .withCodigo(1)
                .withTotal(new BigDecimal("100.50"))
                .withDataPedido("2025-12-13")
                .withStatus("PENDENTE")
                .withItems(Collections.emptyList())
                .build();

        InformacoesPedidoDTO dto2 = InformacoesPedidoDTO.builder()
                .withCodigo(1)
                .withTotal(new BigDecimal("100.50"))
                .withDataPedido("2025-12-13")
                .withStatus("PENDENTE")
                .withItems(Collections.emptyList())
                .build();

        // Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        InformacoesPedidoDTO dto = InformacoesPedidoDTO.builder()
                .withCodigo(1)
                .withTotal(new BigDecimal("100.50"))
                .withDataPedido("2025-12-13")
                .withStatus("PENDENTE")
                .withItems(Collections.emptyList())
                .build();

        // Act
        String result = dto.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("codigo=1"));
        assertTrue(result.contains("total=100.50"));
        assertTrue(result.contains("dataPedido=2025-12-13"));
        assertTrue(result.contains("status=PENDENTE"));
    }
}
