package io.github.hvogel.clientes.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    @Test
    void testEPerfil() {
        assertEquals(3, EPerfil.values().length);
        assertEquals(EPerfil.ROLE_USER, EPerfil.valueOf("ROLE_USER"));
    }

    @Test
    void testSearchOperation() {
        assertNotNull(SearchOperation.values());
        assertEquals(SearchOperation.EQUAL, SearchOperation.valueOf("EQUAL"));
    }

    // Test other status enums simply to ensure values() and valueOf() are covered
    @Test
    void testStatusChamado() {
        assertNotNull(StatusChamado.values());
    }

    @Test
    void testStatusDocumento() {
        assertNotNull(StatusDocumento.values());
    }

    @Test
    void testStatusPedido() {
        assertNotNull(StatusPedido.values());
    }

    @Test
    void testStatusServico() {
        assertNotNull(StatusServico.values());
    }
}
