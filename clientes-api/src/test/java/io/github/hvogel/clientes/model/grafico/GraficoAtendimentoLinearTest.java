package io.github.hvogel.clientes.model.grafico;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class GraficoAtendimentoLinearTest extends BaseEqualsHashCodeTest<GraficoAtendimentoLinear> {

    @Override
    protected GraficoAtendimentoLinear createInstance() {
        return GraficoAtendimentoLinear.builder()
                .withMesAno(Collections.singletonList("A"))
                .build();
    }

    @Override
    protected GraficoAtendimentoLinear createEqualInstance() {
        return GraficoAtendimentoLinear.builder()
                .withMesAno(Collections.singletonList("A"))
                .build();
    }

    @Override
    protected GraficoAtendimentoLinear createDifferentInstance() {
        return GraficoAtendimentoLinear.builder()
                .withMesAno(Collections.singletonList("B"))
                .build();
    }

    @Test
    void testBuilderAndGetters() {
        GraficoAtendimentoLinear grafico = GraficoAtendimentoLinear.builder()
                .withMesAno(Arrays.asList("Jan", "Feb"))
                .withEmAtendimento(Arrays.asList(1, 2))
                .withCancelado(Arrays.asList(0, 1))
                .withFinalizado(Arrays.asList(5, 6))
                .build();

        assertNotNull(grafico);
        assertEquals(2, grafico.getMonthYear().size());
        assertEquals("Jan", grafico.getMonthYear().getFirst());
        assertEquals(1, grafico.getEmAtendimento().getFirst());
        assertEquals(0, grafico.getCancelado().getFirst());
        assertEquals(5, grafico.getFinalizado().getFirst());
    }

    @Test
    void testSetters() {
        GraficoAtendimentoLinear grafico = GraficoAtendimentoLinear.builder().build();
        grafico.setMonthYear(Collections.singletonList("Mar"));
        grafico.setEmAtendimento(Collections.singletonList(10));
        grafico.setCancelado(Collections.singletonList(2));
        grafico.setFinalizado(Collections.singletonList(20));

        assertEquals("Mar", grafico.getMonthYear().getFirst());
        assertEquals(10, grafico.getEmAtendimento().getFirst());
        assertEquals(2, grafico.getCancelado().getFirst());
        assertEquals(20, grafico.getFinalizado().getFirst());
    }
}
