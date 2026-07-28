package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import io.github.hvogel.clientes.model.entity.Pacote;
import io.github.hvogel.clientes.rest.dto.PacoteDTO;
import io.github.hvogel.clientes.service.PacoteService;
import io.github.hvogel.clientes.service.TotalPacotesService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(PacoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class PacoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PacoteService pacoteService;

    @MockBean
    private TotalPacotesService totalPacotesService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testSalvar() throws Exception {
        PacoteDTO dto = new PacoteDTO();
        dto.setDescricao("Pacote Teste");
        dto.setJustificativa("Justificativa Teste");
        dto.setStatus("Ativo");

        when(pacoteService.salvar(any(Pacote.class))).thenReturn(new Pacote());

        mockMvc.perform(post("/api/pacote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAtualizar() throws Exception {
        PacoteDTO dto = new PacoteDTO();
        dto.setDescricao("Pacote Teste");
        dto.setJustificativa("Justificativa Teste");
        dto.setStatus("Ativo");

        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.of(new Pacote()));
        when(pacoteService.atualizar(any(Pacote.class))).thenReturn(new Pacote());

        mockMvc.perform(put("/api/pacote/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletar() throws Exception {
        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.of(new Pacote()));
        doNothing().when(pacoteService).deletar(any(Pacote.class));

        mockMvc.perform(delete("/api/pacote/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testObterTodos() throws Exception {
        when(pacoteService.obterTodos()).thenReturn(Arrays.asList(new Pacote()));

        mockMvc.perform(get("/api/pacote")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAcharPorId() throws Exception {
        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.of(new Pacote()));

        mockMvc.perform(get("/api/pacote/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testObterTotalPacotes() throws Exception {
        when(totalPacotesService.obterTotalPacotes()).thenReturn(10L);

        mockMvc.perform(get("/api/pacote/totalPacotes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testList() throws Exception {
        Page<Pacote> page = new PageImpl<>(Arrays.asList(new Pacote()));
        when(pacoteService.recuperarTodos(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/pacote/pesquisa-paginada")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSalvarErro() throws Exception {
        PacoteDTO dto = new PacoteDTO();
        dto.setDescricao("Pacote Teste");
        dto.setJustificativa("Justificativa Teste");
        dto.setStatus("Ativo");

        when(pacoteService.salvar(any(Pacote.class)))
                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro"));

        mockMvc.perform(post("/api/pacote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAtualizarErro() throws Exception {
        PacoteDTO dto = new PacoteDTO();
        dto.setDescricao("Pacote Teste");
        dto.setJustificativa("Justificativa Teste");
        dto.setStatus("Ativo");

        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.of(new Pacote()));
        when(pacoteService.atualizar(any(Pacote.class)))
                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro"));

        mockMvc.perform(put("/api/pacote/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAtualizarNotFound() throws Exception {
        PacoteDTO dto = new PacoteDTO();
        dto.setDescricao("Pacote Teste");
        dto.setJustificativa("Justificativa Teste");
        dto.setStatus("Ativo");

        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/pacote/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletarNotFound() throws Exception {
        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/pacote/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAcharPorIdNotFound() throws Exception {
        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pacote/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListComFiltros() throws Exception {
        Page<Pacote> page = new PageImpl<>(Arrays.asList(new Pacote()));
        when(pacoteService.pesquisarPelaDescricao(any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/pacote/pesquisa-paginada")
                .param("descricao", "teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testListComFiltroId() throws Exception {
        Page<Pacote> page = new PageImpl<>(Arrays.asList(new Pacote()));
        when(pacoteService.obterPorId(anyInt(), any(Pageable.class))).thenReturn(page);
        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.of(new Pacote()));

        mockMvc.perform(get("/api/pacote/pesquisa-paginada")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testListComFiltroIdNotFound() throws Exception {
        when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pacote/pesquisa-paginada")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListComplexSort() throws Exception {
        Page<Pacote> page = new PageImpl<>(Arrays.asList(new Pacote()));
        when(pacoteService.recuperarTodos(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/pacote/pesquisa-paginada")
                .param("sort", "descricao,desc")
                .param("sort", "id,asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
