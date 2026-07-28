package io.github.hvogel.clientes.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import io.github.hvogel.clientes.enums.StatusServico;

class ServicoPrestadoTest {

    @Test
    void testGettersAndSetters() {
        ServicoPrestado sp = new ServicoPrestado();
        Integer id = 1;
        String descricao = "Desc";
        Cliente cliente = new Cliente();
        BigDecimal valor = BigDecimal.TEN;
        LocalDate data = LocalDate.now();
        LocalDate dataConclusao = LocalDate.now().plusDays(1);
        StatusServico status = StatusServico.F; // Changed from REALIZADO
        String captcha = "captcha";
        Prestador prestador = new Prestador();
        String tipo = "1";
        Natureza natureza = new Natureza();
        Atividade atividade = new Atividade();
        String local = "Local";
        String conclusao = "Conclusao";

        sp.setId(id);
        sp.setDescricao(descricao);
        sp.setCliente(cliente);
        sp.setValor(valor);
        sp.setData(data);
        sp.setDataConclusao(dataConclusao);
        sp.setStatus(status);
        sp.setCaptcha(captcha);
        sp.setPrestador(prestador);
        sp.setTipo(tipo);
        sp.setNatureza(natureza);
        sp.setAtividade(atividade);
        sp.setLocalAtendimento(local);
        sp.setConclusao(conclusao);

        assertEquals(id, sp.getId());
        assertEquals(descricao, sp.getDescricao());
        assertEquals(cliente, sp.getCliente());
        assertEquals(valor, sp.getValor());
        assertEquals(data, sp.getData());
        assertEquals(dataConclusao, sp.getDataConclusao());
        assertEquals(status, sp.getStatus());
        assertEquals(captcha, sp.getCaptcha());
        assertEquals(prestador, sp.getPrestador());
        assertEquals(tipo, sp.getTipo());
        assertEquals(natureza, sp.getNatureza());
        assertEquals(atividade, sp.getAtividade());
        assertEquals(local, sp.getLocalAtendimento());
        assertEquals(conclusao, sp.getConclusao());
    }

    @Test
    void testEqualsAndHashCode() {
        ServicoPrestado sp1 = new ServicoPrestado();
        sp1.setId(1);
        sp1.setDescricao("Desc");

        ServicoPrestado sp2 = new ServicoPrestado();
        sp2.setId(1);
        sp2.setDescricao("Desc");

        assertEquals(sp1, sp2);
        assertEquals(sp1.hashCode(), sp2.hashCode());
        assertEquals(sp1, sp1);
        assertNotEquals(null, sp1);
        assertNotEquals(sp1, new Object());

        sp2.setId(2);
        assertNotEquals(sp1, sp2);

        sp2.setId(1);
        sp2.setDescricao("Diff");
        assertEquals(sp1, sp2); // Mesmo ID = objetos iguais
    }

    @Test
    void testToString() {
        ServicoPrestado sp = new ServicoPrestado();
        sp.setId(1);
        String s = sp.toString();
        assertTrue(s.contains("id=1"));
    }
}
