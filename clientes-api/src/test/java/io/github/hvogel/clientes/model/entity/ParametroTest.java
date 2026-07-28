package io.github.hvogel.clientes.model.entity;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ParametroTest extends BaseEqualsHashCodeTest<Parametro> {

    @Override
    protected Parametro createInstance() {
        Parametro entity = new Parametro();
        entity.setId(1L);
        entity.setDescricao("Param");
        return entity;
    }

    @Override
    protected Parametro createEqualInstance() {
        Parametro entity = new Parametro();
        entity.setId(1L);
        entity.setDescricao("Param");
        return entity;
    }

    @Override
    protected Parametro createDifferentInstance() {
        Parametro entity = new Parametro();
        entity.setId(2L);
        return entity;
    }

    @Test
    void testGettersAndSetters() {
        Parametro entity = new Parametro();

        entity.setId(1L);
        entity.setDescricao("Parametro Teste");
        entity.setData(LocalDate.of(2024, 1, 1));

        assertEquals(1L, entity.getId());
        assertEquals("Parametro Teste", entity.getDescricao());
        assertEquals(LocalDate.of(2024, 1, 1), entity.getData());
    }
}
