package io.github.hvogel.clientes.model.grafico;

import io.github.hvogel.clientes.test.base.BaseEqualsHashCodeTest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class GraficoStatusPacotePercentualTest extends BaseEqualsHashCodeTest<GraficoStatusPacotePercentual> {

    @Override
    protected GraficoStatusPacotePercentual createInstance() {
        return GraficoStatusPacotePercentual.builder()
                .withIniciadoPercentual(Collections.singletonList(BigDecimal.ONE))
                .build();
    }

    @Override
    protected GraficoStatusPacotePercentual createEqualInstance() {
        return GraficoStatusPacotePercentual.builder()
                .withIniciadoPercentual(Collections.singletonList(BigDecimal.ONE))
                .build();
    }

    @Override
    protected GraficoStatusPacotePercentual createDifferentInstance() {
        return GraficoStatusPacotePercentual.builder()
                .withIniciadoPercentual(Collections.singletonList(BigDecimal.TEN))
                .build();
    }

    @Test
    void testBuilderAndGetters() {
        GraficoStatusPacotePercentual grafico = GraficoStatusPacotePercentual.builder()
                .withIniciadoPercentual(Collections.singletonList(BigDecimal.ONE))
                .withAprovadoPercentual(Collections.singletonList(BigDecimal.TEN))
                .withExecutandoPercentual(Collections.singletonList(BigDecimal.ZERO))
                .withCanceladoPercentual(Collections.singletonList(BigDecimal.valueOf(5)))
                .withFinalizadoPercentual(Collections.singletonList(BigDecimal.valueOf(100)))
                .build();

        assertNotNull(grafico);
        assertEquals(BigDecimal.ONE, grafico.getIniciadoPercentual().getFirst());
        assertEquals(BigDecimal.TEN, grafico.getAprovadoPercentual().getFirst());
        assertEquals(BigDecimal.ZERO, grafico.getExecutandoPercentual().getFirst());
        assertEquals(BigDecimal.valueOf(5), grafico.getCanceladoPercentual().getFirst());
        assertEquals(BigDecimal.valueOf(100), grafico.getFinalizadoPercentual().getFirst());
    }

    @Test
    void testSetters() {
        GraficoStatusPacotePercentual grafico = GraficoStatusPacotePercentual.builder().build();
        grafico.setIniciadoPercentual(Collections.singletonList(BigDecimal.ONE));
        grafico.setAprovadoPercentual(Collections.singletonList(BigDecimal.TEN));
        grafico.setExecutandoPercentual(Collections.singletonList(BigDecimal.ZERO));
        grafico.setCanceladoPercentual(Collections.singletonList(BigDecimal.valueOf(5)));
        grafico.setFinalizadoPercentual(Collections.singletonList(BigDecimal.valueOf(100)));

        assertEquals(BigDecimal.ONE, grafico.getIniciadoPercentual().getFirst());
        assertEquals(BigDecimal.TEN, grafico.getAprovadoPercentual().getFirst());
        assertEquals(BigDecimal.ZERO, grafico.getExecutandoPercentual().getFirst());
        assertEquals(BigDecimal.valueOf(5), grafico.getCanceladoPercentual().getFirst());
        assertEquals(BigDecimal.valueOf(100), grafico.getFinalizadoPercentual().getFirst());
    }
}
