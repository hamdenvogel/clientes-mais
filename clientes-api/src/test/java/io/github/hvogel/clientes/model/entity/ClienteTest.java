package io.github.hvogel.clientes.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void testGettersAndSetters() {
        Cliente cliente = new Cliente();

        cliente.setId(1);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678901");
        cliente.setDataCadastro(LocalDate.of(2025, 1, 15));
        cliente.setPix("joao@pix.com");
        cliente.setCep("12345-678");
        cliente.setEndereco("Rua A, 123");
        cliente.setComplemento("Apto 101");
        cliente.setUf("SP");
        cliente.setCidade("São Paulo");
        cliente.setCaptcha("abc123");

        assertEquals(1, cliente.getId());
        assertEquals("João Silva", cliente.getNome());
        assertEquals("12345678901", cliente.getCpf());
        assertEquals(LocalDate.of(2025, 1, 15), cliente.getDataCadastro());
        assertEquals("joao@pix.com", cliente.getPix());
        assertEquals("12345-678", cliente.getCep());
        assertEquals("Rua A, 123", cliente.getEndereco());
        assertEquals("Apto 101", cliente.getComplemento());
        assertEquals("SP", cliente.getUf());
        assertEquals("São Paulo", cliente.getCidade());
        assertEquals("abc123", cliente.getCaptcha());
    }

    @Test
    void testPrePersist() {
        Cliente cliente = new Cliente();

        cliente.prePersist();

        assertNotNull(cliente.getDataCadastro());
        assertEquals(LocalDate.now(), cliente.getDataCadastro());
    }

    @Test
    void testEquals() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1);
        cliente1.setNome("João");
        cliente1.setCpf("12345678901");

        Cliente cliente2 = new Cliente();
        cliente2.setId(1);
        cliente2.setNome("João");
        cliente2.setCpf("12345678901");

        Cliente cliente3 = new Cliente();
        cliente3.setId(2);
        cliente3.setNome("Maria");
        cliente3.setCpf("98765432100");

        assertEquals(cliente1, cliente2);
        assertNotEquals(cliente1, cliente3);
        assertNotEquals(null, cliente1);
        assertNotEquals(cliente1, new Object());
        assertEquals(cliente1, cliente1);
    }

    @Test
    void testHashCode() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1);
        cliente1.setNome("João");
        cliente1.setCpf("12345678901");

        Cliente cliente2 = new Cliente();
        cliente2.setId(1);
        cliente2.setNome("João");
        cliente2.setCpf("12345678901");

        assertEquals(cliente1.hashCode(), cliente2.hashCode());
    }

    @Test
    void testToString() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678901");

        String result = cliente.toString();

        assertNotNull(result);
        assertTrue(result.contains("Cliente"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("nome=João Silva"));
        assertTrue(result.contains("cpf=12345678901"));
    }
}
