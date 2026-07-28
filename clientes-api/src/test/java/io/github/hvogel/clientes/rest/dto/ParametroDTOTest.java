package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ParametroDTOTest extends BaseEqualsHashCodeTest<ParametroDTO> {

    @Override
    protected ParametroDTO createInstance() {
        ParametroDTO dto = new ParametroDTO();
        dto.setId(1);
        dto.setDescricao("Param");
        return dto;
    }

    @Override
    protected ParametroDTO createEqualInstance() {
        ParametroDTO dto = new ParametroDTO();
        dto.setId(1);
        dto.setDescricao("Param");
        return dto;
    }

    @Override
    protected ParametroDTO createDifferentInstance() {
        ParametroDTO dto = new ParametroDTO();
        dto.setId(2);
        return dto;
    }

    @Test
    void testGettersAndSetters() {
        ParametroDTO dto = new ParametroDTO();

        dto.setId(1);
        dto.setDescricao("Parametro Teste");
        dto.setData(LocalDate.of(2024, 1, 1));

        assertEquals(1, dto.getId());
        assertEquals("Parametro Teste", dto.getDescricao());
        assertEquals(LocalDate.of(2024, 1, 1), dto.getData());
    }
}
