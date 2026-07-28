package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

import io.github.hvogel.clientes.model.entity.Profissao;
import io.github.hvogel.clientes.service.ProfissaoService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(ProfissaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfissaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissaoService profissaoService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testObterTodos() throws Exception {
        Profissao profissao = new Profissao();
        profissao.setId(1);
        profissao.setDescricao("Teste");

        when(profissaoService.obterTodos()).thenReturn(Arrays.asList(profissao));

        mockMvc.perform(get("/api/profissao")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Teste"));
    }

    @Test
    void testAcharPorId() throws Exception {
        Profissao profissao = new Profissao();
        profissao.setId(1);
        profissao.setDescricao("Teste");

        when(profissaoService.obterPorId(anyInt())).thenReturn(Optional.of(profissao));

        mockMvc.perform(get("/api/profissao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste"));
    }

    @Test
    void testObterPorDescricao() throws Exception {
        Profissao profissao = new Profissao();
        profissao.setId(1);
        profissao.setDescricao("Teste");

        when(profissaoService.obterPorDescricao(anyString())).thenReturn(Arrays.asList(profissao));

        mockMvc.perform(get("/api/profissao/descricao/Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Teste"));
    }

    @Test
    void testAcharPorId_NotFound() throws Exception {
        when(profissaoService.obterPorId(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profissao/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAcharPorId_BadRequest() throws Exception {
        when(profissaoService.obterPorId(anyInt()))
                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro regra negocio"));

        mockMvc.perform(get("/api/profissao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObterPorDescricao_BadRequest() throws Exception {
        when(profissaoService.obterPorDescricao(anyString()))
                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro regra negocio"));

        mockMvc.perform(get("/api/profissao/descricao/Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
