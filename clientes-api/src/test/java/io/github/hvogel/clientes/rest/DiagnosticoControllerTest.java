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

import io.github.hvogel.clientes.model.entity.Diagnostico;
import io.github.hvogel.clientes.rest.dto.DiagnosticoDTO;
import io.github.hvogel.clientes.service.DiagnosticoService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(DiagnosticoController.class)
@AutoConfigureMockMvc(addFilters = false)
class DiagnosticoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DiagnosticoService service;

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
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        diagnostico.setDescricao("Teste");

        when(service.obterDiagnosticos()).thenReturn(Arrays.asList(diagnostico));

        mockMvc.perform(get("/api/diagnostico/lista-sem-paginacao")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Teste"));
    }

    @Test
    void testObterPorId() throws Exception {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        diagnostico.setDescricao("Teste");

        when(service.obterPorId(anyLong())).thenReturn(Optional.of(diagnostico));

        mockMvc.perform(get("/api/diagnostico/lista-sem-paginacao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste"));
    }

    @Test
    void testAtualizarDescricao() throws Exception {
        DiagnosticoDTO dto = new DiagnosticoDTO();
        dto.setDescricao("Nova Descricao");

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        diagnostico.setDescricao("Nova Descricao");

        when(service.obterPorId(anyLong())).thenReturn(Optional.of(diagnostico));
        when(service.save(any(Diagnostico.class))).thenReturn(diagnostico);

        mockMvc.perform(patch("/api/diagnostico/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Nova Descricao"));
    }

    @Test
    void testTotal() throws Exception {
        when(service.count()).thenReturn(10L);

        mockMvc.perform(get("/api/diagnostico/total")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10));
    }

    @Test
    void testList() throws Exception {
        Page<Diagnostico> page = new PageImpl<>(Arrays.asList(new Diagnostico()));
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/diagnostico/pesquisa-paginada")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testObterPorIdNotFound() throws Exception {
        when(service.obterPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/diagnostico/lista-sem-paginacao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAtualizarDescricaoNotFound() throws Exception {
        DiagnosticoDTO dto = new DiagnosticoDTO();
        dto.setDescricao("Nova Descricao");

        when(service.obterPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/diagnostico/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAtualizarDescricaoBadRequest() throws Exception {
        DiagnosticoDTO dto = new DiagnosticoDTO();
        dto.setDescricao("Nova Descricao");

        // Simulate RegraNegocioException wrapped in RuntimeException or directly thrown
        // if mocking allows
        // Since catching RegraNegocioException is inside the method, we need
        // service.save to throw it or service.obterPorId to trigger logic
        // But the try-catch wraps the map logic.

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setId(1L);
        when(service.obterPorId(anyLong())).thenReturn(Optional.of(diagnostico));
        when(service.save(any(Diagnostico.class)))
                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro regra negocio"));

        mockMvc.perform(patch("/api/diagnostico/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testListComFiltros() throws Exception {
        Page<Diagnostico> page = new PageImpl<>(Arrays.asList(new Diagnostico()));
        when(service.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(any(), any(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/diagnostico/pesquisa-paginada")
                .param("descricao", "teste")
                .param("id-servico-prestado", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testListComFiltroIdServicoPrestado() throws Exception {
        Page<Diagnostico> page = new PageImpl<>(Arrays.asList(new Diagnostico()));
        when(service.findByServicoPrestadoId(any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/diagnostico/pesquisa-paginada")
                .param("id-servico-prestado", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testListComSortComplexo() throws Exception {
        Page<Diagnostico> page = new PageImpl<>(Arrays.asList(new Diagnostico()));
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/diagnostico/pesquisa-paginada")
                .param("sort", "descricao,desc")
                .param("sort", "id,asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
