package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.github.hvogel.clientes.model.entity.Equipamento;
import io.github.hvogel.clientes.service.EquipamentoService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(EquipamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipamentoService service;

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
        Equipamento equipamento = new Equipamento();
        equipamento.setId(1L);
        equipamento.setDescricao("Teste");

        when(service.obterEquipamentos()).thenReturn(Arrays.asList(equipamento));

        mockMvc.perform(get("/api/equipamento/lista-sem-paginacao")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Teste"));
    }

    @Test
    void testObterPorId() throws Exception {
        Equipamento equipamento = new Equipamento();
        equipamento.setId(1L);
        equipamento.setDescricao("Teste");

        when(service.obterPorId(anyLong())).thenReturn(Optional.of(equipamento));

        mockMvc.perform(get("/api/equipamento/lista-sem-paginacao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste"));
    }

    @Test
    void testTotal() throws Exception {
        when(service.count()).thenReturn(10L);

        mockMvc.perform(get("/api/equipamento/total")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10));
    }

    @Test
    void testList() throws Exception {
        Page<Equipamento> page = new PageImpl<>(Arrays.asList(new Equipamento()));
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/equipamento/pesquisa-paginada")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testObterPorIdNotFound() throws Exception {
        when(service.obterPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/equipamento/lista-sem-paginacao/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListWithFilters() throws Exception {
        Page<Equipamento> page = new PageImpl<>(Arrays.asList(new Equipamento()));
        when(service.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(anyString(), anyInt(),
                any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/equipamento/pesquisa-paginada")
                .param("descricao", "Teste")
                .param("id-servico-prestado", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testListWithFilterIdServico() throws Exception {
        Page<Equipamento> page = new PageImpl<>(Arrays.asList(new Equipamento()));
        when(service.findByServicoPrestadoId(anyInt(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/equipamento/pesquisa-paginada")
                .param("id-servico-prestado", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testListWithSortDesc() throws Exception {
        Page<Equipamento> page = new PageImpl<>(Arrays.asList(new Equipamento()));
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/equipamento/pesquisa-paginada")
                .param("sort", "descricao,desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testListWithComplexSort() throws Exception {
        Page<Equipamento> page = new PageImpl<>(Arrays.asList(new Equipamento()));
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/equipamento/pesquisa-paginada")
                .param("sort", "descricao,asc")
                .param("sort", "id,desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
