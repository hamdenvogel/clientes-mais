package io.github.hvogel.clientes.model.entity;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NaturezaTest extends BaseEqualsHashCodeTest<Natureza> {

    @Override
    protected Natureza createInstance() {
        Natureza entity = new Natureza();
        entity.setId(1L);
        entity.setDescricao("Natureza");
        return entity;
    }

    @Override
    protected Natureza createEqualInstance() {
        Natureza entity = new Natureza();
        entity.setId(1L);
        entity.setDescricao("Natureza");
        return entity;
    }

    @Override
    protected Natureza createDifferentInstance() {
        Natureza entity = new Natureza();
        entity.setId(2L);
        return entity;
    }

    @Test
    void testGettersAndSetters() {
        Natureza entity = new Natureza();

        entity.setId(1L);
        entity.setDescricao("Natureza Teste");

        assertEquals(1L, entity.getId());
        assertEquals("Natureza Teste", entity.getDescricao());
    }
}
