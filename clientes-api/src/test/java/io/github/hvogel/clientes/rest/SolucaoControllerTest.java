package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.hvogel.clientes.model.entity.Solucao;
import io.github.hvogel.clientes.rest.dto.SolucaoDTO;
import io.github.hvogel.clientes.service.SolucaoService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(SolucaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class SolucaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolucaoService service;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testObterListaSemPaginacao() {
        Solucao solucao = new Solucao();
        solucao.setId(1L);
        solucao.setDescricao("Teste");

        when(service.obterSolucoes()).thenReturn(Arrays.asList(solucao));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> mockMvc
                .perform(get("/api/solucao/lista-sem-paginacao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Teste")));
    }

    @Test
    void testObterPorId() {
        Solucao solucao = new Solucao();
        solucao.setId(1L);
        solucao.setDescricao("Teste");

        when(service.obterPorId(anyLong())).thenReturn(Optional.of(solucao));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> mockMvc
                .perform(get("/api/solucao/lista-sem-paginacao/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste")));
    }

    @Test
    void testAtualizarDescricao() {
        SolucaoDTO dto = new SolucaoDTO();
        dto.setDescricao("Nova Descricao");

        Solucao solucao = new Solucao();
        solucao.setId(1L);
        solucao.setDescricao("Nova Descricao");

        when(service.obterPorId(anyLong())).thenReturn(Optional.of(solucao));
        when(service.save(any(Solucao.class))).thenReturn(solucao);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> mockMvc.perform(patch("/api/solucao/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Nova Descricao")));
    }

    @Test
    void testTotal() {
        when(service.count()).thenReturn(10L);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> mockMvc.perform(get("/api/solucao/total")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10)));
    }

    @Test
    void testList() {
        Page<Solucao> page = new PageImpl<>(Arrays.asList(new Solucao()));
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> mockMvc.perform(get("/api/solucao/pesquisa-paginada")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()));
    }
}
