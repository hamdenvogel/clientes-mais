package io.github.hvogel.clientes.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PacoteTest {

    @Test
    void testGettersAndSetters() {
        Pacote pacote = new Pacote();

        pacote.setId(1);
        pacote.setDescricao("Pacote de Serviços 2025");
        pacote.setJustificativa("Melhoria de infraestrutura");
        pacote.setData(LocalDate.of(2025, 1, 10));
        pacote.setDataPrevisao(LocalDate.of(2025, 6, 30));
        pacote.setDataConclusao(LocalDate.of(2025, 6, 25));
        pacote.setStatus("A");

        assertEquals(1, pacote.getId());
        assertEquals("Pacote de Serviços 2025", pacote.getDescricao());
        assertEquals("Melhoria de infraestrutura", pacote.getJustificativa());
        assertEquals(LocalDate.of(2025, 1, 10), pacote.getData());
        assertEquals(LocalDate.of(2025, 6, 30), pacote.getDataPrevisao());
        assertEquals(LocalDate.of(2025, 6, 25), pacote.getDataConclusao());
        assertEquals("A", pacote.getStatus());
    }

    @Test
    void testPrePersist() {
        Pacote pacote = new Pacote();

        pacote.prePersist();

        assertNotNull(pacote.getData());
        assertEquals(LocalDate.now(), pacote.getData());
    }

    @Test
    void testEquals() {
        Pacote pacote1 = new Pacote();
        pacote1.setId(1);
        pacote1.setDescricao("Pacote A");
        pacote1.setStatus("A");

        Pacote pacote2 = new Pacote();
        pacote2.setId(1);
        pacote2.setDescricao("Pacote A");
        pacote2.setStatus("A");

        Pacote pacote3 = new Pacote();
        pacote3.setId(2);
        pacote3.setDescricao("Pacote B");
        pacote3.setStatus("F");

        assertEquals(pacote1, pacote2);
        assertNotEquals(pacote1, pacote3);
        assertNotEquals(null, pacote1);
        assertNotEquals(pacote1, new Object());
        assertEquals(pacote1, pacote1);
    }

    @Test
    void testHashCode() {
        Pacote pacote1 = new Pacote();
        pacote1.setId(1);
        pacote1.setDescricao("Pacote A");

        Pacote pacote2 = new Pacote();
        pacote2.setId(1);
        pacote2.setDescricao("Pacote A");

        assertEquals(pacote1.hashCode(), pacote2.hashCode());
    }

    @Test
    void testToString() {
        Pacote pacote = new Pacote();
        pacote.setId(1);
        pacote.setDescricao("Pacote Teste");
        pacote.setStatus("A");

        String result = pacote.toString();

        assertNotNull(result);
        assertTrue(result.contains("Pacote"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("descricao=Pacote Teste"));
        assertTrue(result.contains("status=A"));
    }
}
