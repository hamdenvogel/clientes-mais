package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.github.hvogel.clientes.model.entity.Atividade;
import io.github.hvogel.clientes.service.AtividadeService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(AtividadeController.class)
@AutoConfigureMockMvc(addFilters = false)
class AtividadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtividadeService service;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testObterListaSemPaginacao() throws Exception {
        Atividade atividade = new Atividade();
        atividade.setId(1L);
        atividade.setDescricao("Teste");

        when(service.obterAtividades()).thenReturn(Arrays.asList(atividade));

        mockMvc.perform(get("/api/atividade/lista-sem-paginacao")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Teste"));
    }

    @Test
    void testObterPorId() throws Exception {
        Atividade atividade = new Atividade();
        atividade.setId(1L);
        atividade.setDescricao("Teste");

        when(service.obterPorId(anyLong())).thenReturn(Optional.of(atividade));

        mockMvc.perform(get("/api/atividade/lista-sem-paginacao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste"));
    }

    @Test
    void testObterPorId_NotFound() throws Exception {
        when(service.obterPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/atividade/lista-sem-paginacao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
