package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ProdutoDTOTest {

    @Test
    void testGettersAndSetters() {
        ProdutoDTO dto = new ProdutoDTO();

        dto.setId(1);
        dto.setDescricao("Produto Teste");
        dto.setPreco(new BigDecimal("100.00"));
        dto.setMarca("Marca Teste");
        dto.setModelo("Modelo Teste");
        dto.setAnoFabricacao(2024);
        dto.setAnoModelo(2024);

        assertEquals(1, dto.getId());
        assertEquals("Produto Teste", dto.getDescricao());
        assertEquals(new BigDecimal("100.00"), dto.getPreco());
        assertEquals("Marca Teste", dto.getMarca());
        assertEquals("Modelo Teste", dto.getModelo());
        assertEquals(2024, dto.getAnoFabricacao());
        assertEquals(2024, dto.getAnoModelo());
    }

    @Test
    void testEquals() {
        ProdutoDTO dto1 = new ProdutoDTO();
        dto1.setId(1);
        dto1.setDescricao("Produto");

        ProdutoDTO dto2 = new ProdutoDTO();
        dto2.setId(1);
        dto2.setDescricao("Produto");

        ProdutoDTO dto3 = new ProdutoDTO();
        dto3.setId(2);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        ProdutoDTO dto1 = new ProdutoDTO();
        dto1.setId(1);

        ProdutoDTO dto2 = new ProdutoDTO();
        dto2.setId(1);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setDescricao("Produto Teste");

        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("Produto Teste"));
    }
}
