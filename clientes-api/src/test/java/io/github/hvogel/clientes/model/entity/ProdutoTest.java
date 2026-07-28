package io.github.hvogel.clientes.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ProdutoTest {

    @Test
    void testGettersAndSetters() {
        Produto p = new Produto();
        Integer id = 1;
        String descricao = "Desc";
        BigDecimal preco = BigDecimal.ONE;
        String marca = "Marca";
        String modelo = "Modelo";
        Integer anoFab = 2020;
        Integer anoMod = 2021;

        p.setId(id);
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setMarca(marca);
        p.setModelo(modelo);
        p.setAnoFabricacao(anoFab);
        p.setAnoModelo(anoMod);

        assertEquals(id, p.getId());
        assertEquals(descricao, p.getDescricao());
        assertEquals(preco, p.getPreco());
        assertEquals(marca, p.getMarca());
        assertEquals(modelo, p.getModelo());
        assertEquals(anoFab, p.getAnoFabricacao());
        assertEquals(anoMod, p.getAnoModelo());
    }

    @Test
    void testEqualsAndHashCode() {
        Produto p1 = new Produto();
        p1.setId(1);
        p1.setDescricao("A");

        Produto p2 = new Produto();
        p2.setId(1);
        p2.setDescricao("A");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        p2.setId(2);
        assertNotEquals(p1, p2);

        p2.setId(1);
        p2.setDescricao("B");
        assertEquals(p1, p2); // Mesmo ID = objetos iguais

        p2.setDescricao("A");
        p2.setPreco(BigDecimal.TEN);
        // p1 preco is null, mas mesmo ID = objetos iguais
        assertEquals(p1, p2);
    }
}
