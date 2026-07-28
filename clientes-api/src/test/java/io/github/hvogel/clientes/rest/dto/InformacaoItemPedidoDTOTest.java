package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class InformacaoItemPedidoDTOTest {

    @Test
    void testBuilder() {
        // Arrange
        String descricaoProduto = "Produto Teste";
        BigDecimal precoUnitario = new BigDecimal("50.99");
        Integer quantidade = 3;

        // Act
        InformacaoItemPedidoDTO dto = InformacaoItemPedidoDTO.builder()
                .withDescricaoProduto(descricaoProduto)
                .withPrecoUnitario(precoUnitario)
                .withQuantidade(quantidade)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(descricaoProduto, dto.getDescricaoProduto());
        assertEquals(precoUnitario, dto.getPrecoUnitario());
        assertEquals(quantidade, dto.getQuantidade());
    }

    @Test
    void testBuilderWithNullValues() {
        // Act
        InformacaoItemPedidoDTO dto = InformacaoItemPedidoDTO.builder().build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getDescricaoProduto());
        assertNull(dto.getPrecoUnitario());
        assertNull(dto.getQuantidade());
    }

    @Test
    void testSetters() {
        // Arrange
        InformacaoItemPedidoDTO dto = InformacaoItemPedidoDTO.builder().build();
        String descricaoProduto = "Outro Produto";
        BigDecimal precoUnitario = new BigDecimal("100.00");
        Integer quantidade = 5;

        // Act
        dto.setDescricaoProduto(descricaoProduto);
        dto.setPrecoUnitario(precoUnitario);
        dto.setQuantidade(quantidade);

        // Assert
        assertEquals(descricaoProduto, dto.getDescricaoProduto());
        assertEquals(precoUnitario, dto.getPrecoUnitario());
        assertEquals(quantidade, dto.getQuantidade());
    }

    @Test
    void testEquals() {
        // Arrange
        InformacaoItemPedidoDTO dto1 = InformacaoItemPedidoDTO.builder()
                .withDescricaoProduto("Produto A")
                .withPrecoUnitario(new BigDecimal("50.99"))
                .withQuantidade(3)
                .build();

        InformacaoItemPedidoDTO dto2 = InformacaoItemPedidoDTO.builder()
                .withDescricaoProduto("Produto A")
                .withPrecoUnitario(new BigDecimal("50.99"))
                .withQuantidade(3)
                .build();

        InformacaoItemPedidoDTO dto3 = InformacaoItemPedidoDTO.builder()
                .withDescricaoProduto("Produto B")
                .withPrecoUnitario(new BigDecimal("75.50"))
                .withQuantidade(2)
                .build();

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1, dto1);
        assertNotEquals(dto1, dto3);
        assertNotEquals(null, dto1);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void testHashCode() {
        // Arrange
        InformacaoItemPedidoDTO dto1 = InformacaoItemPedidoDTO.builder()
                .withDescricaoProduto("Produto A")
                .withPrecoUnitario(new BigDecimal("50.99"))
                .withQuantidade(3)
                .build();

        InformacaoItemPedidoDTO dto2 = InformacaoItemPedidoDTO.builder()
                .withDescricaoProduto("Produto A")
                .withPrecoUnitario(new BigDecimal("50.99"))
                .withQuantidade(3)
                .build();

        // Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        InformacaoItemPedidoDTO dto = InformacaoItemPedidoDTO.builder()
                .withDescricaoProduto("Produto Teste")
                .withPrecoUnitario(new BigDecimal("50.99"))
                .withQuantidade(3)
                .build();

        // Act
        String result = dto.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("descricaoProduto=Produto Teste"));
        assertTrue(result.contains("precoUnitario=50.99"));
        assertTrue(result.contains("quantidade=3"));
    }

    @Test
    void testEqualsWithNullFields() {
        // Arrange
        InformacaoItemPedidoDTO dto1 = InformacaoItemPedidoDTO.builder().build();
        InformacaoItemPedidoDTO dto2 = InformacaoItemPedidoDTO.builder().build();

        // Assert
        assertEquals(dto1, dto2);
    }
}
