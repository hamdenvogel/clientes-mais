package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.hvogel.clientes.model.entity.Chamado;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.mapper.ChamadoMapper;
import io.github.hvogel.clientes.rest.dto.ChamadoDTO;
import io.github.hvogel.clientes.service.ChamadoService;
import io.github.hvogel.clientes.service.ClienteService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(ChamadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChamadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChamadoService chamadoService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ChamadoMapper chamadoMapper;

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
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);
        dto.setDescricao("Descricao");
        dto.setLocalAcontecimento("Local");
        dto.setStatus(io.github.hvogel.clientes.enums.StatusChamado.A);

        Chamado chamado = new Chamado();
        chamado.setId(1L);

        when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
        when(chamadoMapper.toEntity(any(ChamadoDTO.class))).thenReturn(chamado);
        when(chamadoService.salvar(any(Chamado.class))).thenReturn(chamado);
        when(chamadoMapper.toDto(any(Chamado.class))).thenReturn(dto);

        mockMvc.perform(post("/api/chamado/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Descricao"));
    }

    @Test
    void testAtualizar() throws Exception {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setClienteId(1);
        dto.setDescricao("Descricao");
        dto.setLocalAcontecimento("Local");
        dto.setStatus(io.github.hvogel.clientes.enums.StatusChamado.A);

        Chamado chamado = new Chamado();
        chamado.setId(1L);

        when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
        when(chamadoService.findOneById(anyLong())).thenReturn(Optional.of(chamado));
        when(chamadoMapper.toEntity(any(ChamadoDTO.class))).thenReturn(chamado);
        when(chamadoService.salvar(any(Chamado.class))).thenReturn(chamado);
        when(chamadoMapper.toDto(any(Chamado.class))).thenReturn(dto);

        mockMvc.perform(put("/api/chamado/alterar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Descricao"));
    }

    @Test
    void testDeletar() throws Exception {
        when(chamadoService.findOneById(anyLong())).thenReturn(Optional.of(new Chamado()));
        doNothing().when(chamadoService).deleteById(anyLong());

        mockMvc.perform(delete("/api/chamado/deletar/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
