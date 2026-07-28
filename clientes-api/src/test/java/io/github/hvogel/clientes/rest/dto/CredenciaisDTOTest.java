package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CredenciaisDTOTest extends BaseEqualsHashCodeTest<CredenciaisDTO> {

    @Override
    protected CredenciaisDTO createInstance() {
        CredenciaisDTO dto = new CredenciaisDTO();
        dto.setLogin("user");
        dto.setSenha("pass");
        return dto;
    }

    @Override
    protected CredenciaisDTO createEqualInstance() {
        CredenciaisDTO dto = new CredenciaisDTO();
        dto.setLogin("user");
        dto.setSenha("pass");
        return dto;
    }

    @Override
    protected CredenciaisDTO createDifferentInstance() {
        CredenciaisDTO dto = new CredenciaisDTO();
        dto.setLogin("other");
        dto.setSenha("other");
        return dto;
    }

    @Test
    void testGettersAndSetters() {
        CredenciaisDTO dto = new CredenciaisDTO();

        dto.setLogin("usuario@test.com");
        dto.setSenha("senha123");

        assertEquals("usuario@test.com", dto.getLogin());
        assertEquals("senha123", dto.getSenha());
    }
}
