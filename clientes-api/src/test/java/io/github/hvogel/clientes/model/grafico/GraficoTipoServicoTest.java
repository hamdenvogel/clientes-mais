package io.github.hvogel.clientes.model.grafico;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class GraficoTipoServicoTest extends BaseEqualsHashCodeTest<GraficoTipoServico> {

    @Override
    protected GraficoTipoServico createInstance() {
        return GraficoTipoServico.builder()
                .withMesAno(Collections.singletonList("A"))
                .build();
    }

    @Override
    protected GraficoTipoServico createEqualInstance() {
        return GraficoTipoServico.builder()
                .withMesAno(Collections.singletonList("A"))
                .build();
    }

    @Override
    protected GraficoTipoServico createDifferentInstance() {
        return GraficoTipoServico.builder()
                .withMesAno(Collections.singletonList("B"))
                .build();
    }

    @Test
    void testBuilderAndGetters() {
        GraficoTipoServico grafico = GraficoTipoServico.builder()
                .withMesAno(Collections.singletonList("Jan"))
                .withUnitario(Collections.singletonList(10))
                .withPacote(Collections.singletonList(20))
                .build();

        assertNotNull(grafico);
        assertEquals("Jan", grafico.getMonthYear().getFirst());
        assertEquals(10, grafico.getUnitario().getFirst());
        assertEquals(20, grafico.getPacote().getFirst());
    }

    @Test
    void testSetters() {
        GraficoTipoServico grafico = GraficoTipoServico.builder().build();
        grafico.setMonthYear(Collections.singletonList("Feb"));
        grafico.setUnitario(Collections.singletonList(30));
        grafico.setPacote(Collections.singletonList(40));

        assertEquals("Feb", grafico.getMonthYear().getFirst());
        assertEquals(30, grafico.getUnitario().getFirst());
        assertEquals(40, grafico.getPacote().getFirst());
    }
}
