package io.github.hvogel.clientes.model.grafico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class GraficoTest {

        @Test
        void testGraficoStatusPacotePercentual() {
                List<BigDecimal> list = Collections.singletonList(BigDecimal.TEN);
                List<BigDecimal> list2 = Collections.singletonList(BigDecimal.ONE);

                GraficoStatusPacotePercentual g1 = GraficoStatusPacotePercentual.builder()
                                .withIniciadoPercentual(list)
                                .withAprovadoPercentual(list)
                                .withExecutandoPercentual(list)
                                .withCanceladoPercentual(list)
                                .withFinalizadoPercentual(list)
                                .build();

                assertEquals(list, g1.getIniciadoPercentual());
                assertEquals(list, g1.getAprovadoPercentual());
                assertEquals(list, g1.getExecutandoPercentual());
                assertEquals(list, g1.getCanceladoPercentual());
                assertEquals(list, g1.getFinalizadoPercentual());

                g1.setIniciadoPercentual(list2);
                g1.setAprovadoPercentual(list2);
                g1.setExecutandoPercentual(list2);
                g1.setCanceladoPercentual(list2);
                g1.setFinalizadoPercentual(list2);

                assertEquals(list2, g1.getIniciadoPercentual());
                assertEquals(list2, g1.getAprovadoPercentual());
                assertEquals(list2, g1.getExecutandoPercentual());
                assertEquals(list2, g1.getCanceladoPercentual());
                assertEquals(list2, g1.getFinalizadoPercentual());

                assertNotNull(g1.toString());

                // Equals and HashCode
                GraficoStatusPacotePercentual g2 = GraficoStatusPacotePercentual.builder()
                                .withIniciadoPercentual(list2)
                                .withAprovadoPercentual(list2)
                                .withExecutandoPercentual(list2)
                                .withCanceladoPercentual(list2)
                                .withFinalizadoPercentual(list2)
                                .build();

                assertEquals(g1, g2);
                assertEquals(g1.hashCode(), g2.hashCode());

                assertNotEquals(null, g1);
                assertNotEquals(g1, new Object());

                // Branch coverage for different fields
                assertNotEquals(g1, GraficoStatusPacotePercentual.builder().withIniciadoPercentual(list).build());
                assertNotEquals(g1, GraficoStatusPacotePercentual.builder().withAprovadoPercentual(list).build());
                assertNotEquals(g1, GraficoStatusPacotePercentual.builder().withExecutandoPercentual(list).build());
                assertNotEquals(g1, GraficoStatusPacotePercentual.builder().withCanceladoPercentual(list).build());
                assertNotEquals(g1, GraficoStatusPacotePercentual.builder().withFinalizadoPercentual(list).build());

                GraficoStatusPacotePercentual g3 = GraficoStatusPacotePercentual.builder().build();
                assertNotEquals(g1, g3);
        }

        @Test
        void testGraficoAtendimentoLinear() {
                List<String> months = Collections.singletonList("Jan");
                List<Integer> counts = Collections.singletonList(5);
                List<String> months2 = Collections.singletonList("Feb");
                List<Integer> counts2 = Collections.singletonList(10);

                GraficoAtendimentoLinear g1 = GraficoAtendimentoLinear.builder()
                                .withMesAno(months)
                                .withEmAtendimento(counts)
                                .withCancelado(counts)
                                .withFinalizado(counts)
                                .build();

                assertEquals(months, g1.getMonthYear());
                assertEquals(counts, g1.getEmAtendimento());
                assertEquals(counts, g1.getCancelado());
                assertEquals(counts, g1.getFinalizado());

                g1.setMonthYear(months2);
                g1.setEmAtendimento(counts2);
                g1.setCancelado(counts2);
                g1.setFinalizado(counts2);

                assertEquals(months2, g1.getMonthYear());
                assertEquals(counts2, g1.getEmAtendimento());
                assertEquals(counts2, g1.getCancelado());
                assertEquals(counts2, g1.getFinalizado());

                assertNotNull(g1.toString());

                // Equals and HashCode
                GraficoAtendimentoLinear g2 = GraficoAtendimentoLinear.builder()
                                .withMesAno(months2)
                                .withEmAtendimento(counts2)
                                .withCancelado(counts2)
                                .withFinalizado(counts2)
                                .build();

                assertEquals(g1, g2);
                assertEquals(g1.hashCode(), g2.hashCode());

                assertNotEquals(null, g1);
                assertNotEquals(g1, new Object());

                // Branch coverage for different fields
                assertNotEquals(g1, GraficoAtendimentoLinear.builder().withMesAno(months).build());
                assertNotEquals(g1, GraficoAtendimentoLinear.builder().withEmAtendimento(counts).build());
                assertNotEquals(g1, GraficoAtendimentoLinear.builder().withCancelado(counts).build());
                assertNotEquals(g1, GraficoAtendimentoLinear.builder().withFinalizado(counts).build());

                GraficoAtendimentoLinear g3 = GraficoAtendimentoLinear.builder().build();
                assertNotEquals(g1, g3);
        }

        @Test
        void testGraficoTipoServico() {
                List<String> months = Collections.singletonList("Jan");
                List<Integer> counts = Collections.singletonList(10);
                List<String> months2 = Collections.singletonList("Feb");
                List<Integer> counts2 = Collections.singletonList(20);

                GraficoTipoServico g1 = GraficoTipoServico.builder()
                                .withMesAno(months)
                                .withUnitario(counts)
                                .withPacote(counts)
                                .build();

                assertEquals(months, g1.getMonthYear());
                assertEquals(counts, g1.getUnitario());
                assertEquals(counts, g1.getPacote());

                g1.setMonthYear(months2);
                g1.setUnitario(counts2);
                g1.setPacote(counts2);

                assertEquals(months2, g1.getMonthYear());
                assertEquals(counts2, g1.getUnitario());
                assertEquals(counts2, g1.getPacote());

                assertNotNull(g1.toString());

                // Equals and HashCode
                GraficoTipoServico g2 = GraficoTipoServico.builder()
                                .withMesAno(months2)
                                .withUnitario(counts2)
                                .withPacote(counts2)
                                .build();

                assertEquals(g1, g2);
                assertEquals(g1.hashCode(), g2.hashCode());

                assertNotEquals(null, g1);
                assertNotEquals(g1, new Object());

                // Branch coverage for different fields
                assertNotEquals(g1, GraficoTipoServico.builder().withMesAno(months).build());
                assertNotEquals(g1, GraficoTipoServico.builder().withUnitario(counts).build());
                assertNotEquals(g1, GraficoTipoServico.builder().withPacote(counts).build());

                GraficoTipoServico g3 = GraficoTipoServico.builder().build();
                assertNotEquals(g1, g3);
        }

        @Test
        void testGraficoAtendimentoTorta() {
                List<String> status = Collections.singletonList("S");
                List<Integer> qtd = Collections.singletonList(3);
                List<String> status2 = Collections.singletonList("S2");
                List<Integer> qtd2 = Collections.singletonList(5);

                GraficoAtendimentoTorta g1 = GraficoAtendimentoTorta.builder()
                                .withStatusAtendimento(status)
                                .withQuantidade(qtd)
                                .build();

                assertEquals(status, g1.getStatusAtendimento());
                assertEquals(qtd, g1.getQuantidade());

                g1.setStatusAtendimento(status2);
                g1.setQuantidade(qtd2);

                assertEquals(status2, g1.getStatusAtendimento());
                assertEquals(qtd2, g1.getQuantidade());

                assertNotNull(g1.toString());

                // Equals and HashCode
                GraficoAtendimentoTorta g2 = GraficoAtendimentoTorta.builder()
                                .withStatusAtendimento(status2)
                                .withQuantidade(qtd2)
                                .build();

                assertEquals(g1, g2);
                assertEquals(g1.hashCode(), g2.hashCode());

                assertNotEquals(null, g1);
                assertNotEquals(g1, new Object());

                // Branch coverage for different fields
                assertNotEquals(g1, GraficoAtendimentoTorta.builder().withStatusAtendimento(status).build());
                assertNotEquals(g1, GraficoAtendimentoTorta.builder().withQuantidade(qtd).build());

                GraficoAtendimentoTorta g3 = GraficoAtendimentoTorta.builder().build();
                assertNotEquals(g1, g3);
        }
}
