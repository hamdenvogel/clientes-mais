package io.github.hvogel.clientes.model.grafico;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class GraficoAtendimentoTortaTest extends BaseEqualsHashCodeTest<GraficoAtendimentoTorta> {

    @Override
    protected GraficoAtendimentoTorta createInstance() {
        return GraficoAtendimentoTorta.builder()
                .withStatusAtendimento(Collections.singletonList("A"))
                .build();
    }

    @Override
    protected GraficoAtendimentoTorta createEqualInstance() {
        return GraficoAtendimentoTorta.builder()
                .withStatusAtendimento(Collections.singletonList("A"))
                .build();
    }

    @Override
    protected GraficoAtendimentoTorta createDifferentInstance() {
        return GraficoAtendimentoTorta.builder()
                .withStatusAtendimento(Collections.singletonList("B"))
                .build();
    }

    @Test
    void testBuilderAndGetters() {
        GraficoAtendimentoTorta grafico = GraficoAtendimentoTorta.builder()
                .withStatusAtendimento(Collections.singletonList("Label"))
                .withQuantidade(Collections.singletonList(10))
                .build();

        assertNotNull(grafico);
        assertEquals("Label", grafico.getStatusAtendimento().getFirst());
        assertEquals(10, grafico.getQuantidade().getFirst());
    }

    @Test
    void testSetters() {
        GraficoAtendimentoTorta grafico = GraficoAtendimentoTorta.builder().build();
        grafico.setStatusAtendimento(Collections.singletonList("New Label"));
        grafico.setQuantidade(Collections.singletonList(20));

        assertEquals("New Label", grafico.getStatusAtendimento().getFirst());
        assertEquals(20, grafico.getQuantidade().getFirst());
    }
}
