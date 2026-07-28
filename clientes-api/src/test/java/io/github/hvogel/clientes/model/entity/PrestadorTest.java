package io.github.hvogel.clientes.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class PrestadorTest {

    @Test
    void testGettersAndSetters() {
        Prestador p = new Prestador();
        Integer id = 1;
        String nome = "Nome";
        String cpf = "11122233344";
        String email = "email@test.com";
        String pix = "pixKey";
        Integer avaliacao = 5;
        Profissao profissao = new Profissao();
        LocalDate data = LocalDate.now();

        p.setId(id);
        p.setNome(nome);
        p.setCpf(cpf);
        p.setEmail(email);
        p.setPix(pix);
        p.setAvaliacao(avaliacao);
        p.setProfissao(profissao);
        p.setDataCadastro(data);

        assertEquals(id, p.getId());
        assertEquals(nome, p.getNome());
        assertEquals(cpf, p.getCpf());
        assertEquals(email, p.getEmail());
        assertEquals(pix, p.getPix());
        assertEquals(avaliacao, p.getAvaliacao());
        assertEquals(profissao, p.getProfissao());
        assertEquals(data, p.getDataCadastro());
    }

    @Test
    void testPrePersist() {
        Prestador p = new Prestador();
        p.prePersist();
        assertNotEquals(null, p.getDataCadastro());
    }

    @Test
    void testEqualsAndHashCode() {
        Prestador p1 = new Prestador();
        p1.setId(1);
        p1.setNome("A");

        Prestador p2 = new Prestador();
        p2.setId(1);
        p2.setNome("A");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        p2.setId(2);
        assertNotEquals(p1, p2);

        p2.setId(1);
        p2.setNome("B");
        assertEquals(p1, p2); // Mesmo ID = objetos iguais
    }
}
