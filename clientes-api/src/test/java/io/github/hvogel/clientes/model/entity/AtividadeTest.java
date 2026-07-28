package io.github.hvogel.clientes.model.entity;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AtividadeTest extends BaseEqualsHashCodeTest<Atividade> {

    @Override
    protected Atividade createInstance() {
        Atividade entity = new Atividade();
        entity.setId(1L);
        entity.setDescricao("Atividade");
        return entity;
    }

    @Override
    protected Atividade createEqualInstance() {
        Atividade entity = new Atividade();
        entity.setId(1L);
        entity.setDescricao("Atividade");
        return entity;
    }

    @Override
    protected Atividade createDifferentInstance() {
        Atividade entity = new Atividade();
        entity.setId(2L);
        return entity;
    }

    @Test
    void testGettersAndSetters() {
        Atividade entity = new Atividade();

        entity.setId(1L);
        entity.setDescricao("Atividade Teste");

        assertEquals(1L, entity.getId());
        assertEquals("Atividade Teste", entity.getDescricao());
    }
}
