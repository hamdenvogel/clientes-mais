package io.github.hvogel.clientes.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.github.hvogel.clientes.service.PacoteService;
import io.github.hvogel.clientes.service.ServicoPrestadoService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(GraficoController.class)
@AutoConfigureMockMvc(addFilters = false)
class GraficoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicoPrestadoService servicoService;

    @MockBean
    private PacoteService pacoteService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testGerarGraficoStatusAtendimentoPorPeriodo() throws Exception {
        when(servicoService.mesesGrafico()).thenReturn(Arrays.asList("Jan"));
        when(servicoService.emAtendimento()).thenReturn(Arrays.asList(1));
        when(servicoService.finalizado()).thenReturn(Arrays.asList(1));
        when(servicoService.cancelado()).thenReturn(Arrays.asList(1));

        mockMvc.perform(get("/api/grafico/grafico-status-atendimento-por-periodo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGerarGraficoStatusAtendimentoQuantidade() throws Exception {
        when(servicoService.descricaoStatus()).thenReturn(Arrays.asList("Aberto"));
        when(servicoService.quantidadeServicos()).thenReturn(Arrays.asList(1));

        mockMvc.perform(get("/api/grafico/grafico-status-atendimento-quantidade")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGerarGraficoStatusPacotePercentual() throws Exception {
        when(pacoteService.iPercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));
        when(pacoteService.aPercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));
        when(pacoteService.ePercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));
        when(pacoteService.cPercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));
        when(pacoteService.fPercentual()).thenReturn(Arrays.asList(BigDecimal.TEN));

        mockMvc.perform(get("/api/grafico/grafico-status-pacote-percentual")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGerarGraficoTipoServicoPorPeriodo() throws Exception {
        when(servicoService.mesesGraficoTipoServico()).thenReturn(Arrays.asList("Jan"));
        when(servicoService.tipoUnitario()).thenReturn(Arrays.asList(1));
        when(servicoService.tipoPacote()).thenReturn(Arrays.asList(1));

        mockMvc.perform(get("/api/grafico/grafico-tipo-servico")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
