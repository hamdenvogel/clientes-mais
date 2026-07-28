package io.github.hvogel.clientes.rest;

import io.github.hvogel.clientes.model.entity.ItemPacote;
import io.github.hvogel.clientes.model.entity.Pacote;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.ItemPacoteDTO;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.service.ItemPacoteService;
import io.github.hvogel.clientes.service.PacoteService;
import io.github.hvogel.clientes.service.ServicoPrestadoService;
import io.github.hvogel.clientes.service.TotalItensPacotesService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ItemPacoteController.class)
@WithMockUser
class ItemPacoteControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ItemPacoteService itemPacoteService;

        @MockBean
        private PacoteService pacoteService;

        @MockBean
        private ServicoPrestadoService servicoPrestadoService;

        @MockBean
        private TotalItensPacotesService totalItensPacotesService;

        @MockBean
        private UserDetailsServiceImpl userDetailsService;

        @MockBean
        private JwtUtils jwtUtils;

        @MockBean
        private AuthEntryPointJwt unauthorizedHandler;

        @MockBean
        private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testSalvar() throws Exception {
                ItemPacoteDTO dto = new ItemPacoteDTO();
                dto.setIdPacote(1);
                dto.setIdServicoPrestado(1);

                when(pacoteService.obterPorId(1)).thenReturn(Optional.of(new Pacote()));
                when(servicoPrestadoService.obterPorId(1)).thenReturn(Optional.of(new ServicoPrestado()));
                when(itemPacoteService.obterPorPacoteEServicoPrestado(1, 1)).thenReturn(Optional.empty());

                mockMvc.perform(post("/api/item-pacote")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated());
        }

        @Test
        void testAtualizar() throws Exception {
                ItemPacoteDTO dto = new ItemPacoteDTO();
                dto.setIdPacote(1);
                dto.setIdServicoPrestado(1);

                ItemPacote itemPacote = new ItemPacote();
                itemPacote.setId(1);

                when(pacoteService.obterPorId(anyInt())).thenReturn(Optional.of(new Pacote()));
                when(servicoPrestadoService.obterPorId(anyInt())).thenReturn(Optional.of(new ServicoPrestado()));
                when(itemPacoteService.obterPorId(anyInt())).thenReturn(Optional.of(itemPacote));
                when(itemPacoteService.atualizar(any())).thenReturn(itemPacote);

                mockMvc.perform(put("/api/item-pacote/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk());
        }

        @Test
        void testDeletar() throws Exception {
                ItemPacote itemPacote = new ItemPacote();
                itemPacote.setId(1);

                when(itemPacoteService.obterPorId(1)).thenReturn(Optional.of(itemPacote));

                mockMvc.perform(delete("/api/item-pacote/1")
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.mensagem").value("Serviço atualizado ao Pacote com sucesso."));
        }

        @Test
        void testDeletarPorPacoteEServicoPrestado() throws Exception {
                Integer idPacote = 1;
                Integer idServico = 1;

                ItemPacote itemPacote = new ItemPacote();
                Pacote pacote = new Pacote();
                pacote.setId(idPacote);
                ServicoPrestado servico = new ServicoPrestado();
                servico.setId(idServico);
                itemPacote.setPacote(pacote);
                itemPacote.setServicoPrestado(servico);

                when(pacoteService.obterPorId(idPacote)).thenReturn(Optional.of(pacote));
                when(servicoPrestadoService.obterPorId(idServico)).thenReturn(Optional.of(servico));
                when(itemPacoteService.obterPorPacoteEServicoPrestado(idPacote, idServico))
                                .thenReturn(Optional.of(itemPacote));

                mockMvc.perform(delete("/api/item-pacote/pacote/" + idPacote + "/servico/" + idServico)
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.mensagem").value("Item do pacote deletado com sucesso."));
        }

        @Test
        void testList() throws Exception {
                when(itemPacoteService.recuperarTodos(any(org.springframework.data.domain.Pageable.class)))
                                .thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/item-pacote/pesquisa-paginada")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testListWithComplexSort() throws Exception {
                when(itemPacoteService.recuperarTodos(any(org.springframework.data.domain.Pageable.class)))
                                .thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/item-pacote/pesquisa-paginada")
                                .param("sort", "pacote.id,desc")
                                .param("sort", "servicoPrestado.descricao,asc")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testListWithFilter() throws Exception {
                when(itemPacoteService.obterPorIdPacote(anyInt(), any(org.springframework.data.domain.Pageable.class)))
                                .thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/item-pacote/pesquisa-paginada")
                                .param("pacote", "1")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testObterTotalItensPorPacote() throws Exception {
                when(totalItensPacotesService.obterTotalItensPacotes(1)).thenReturn(5L);

                mockMvc.perform(get("/api/item-pacote/total-por-pacote/1")
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalItensPacotes").value(5));

        }

        @Test
        void testSalvarPacoteInexistente() throws Exception {
                ItemPacoteDTO dto = new ItemPacoteDTO();
                dto.setIdPacote(1);
                dto.setIdServicoPrestado(1);

                when(pacoteService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(post("/api/item-pacote")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertEquals("Pacote Inexistente",
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()));
        }

        @Test
        void testSalvarServicoInexistente() throws Exception {
                ItemPacoteDTO dto = new ItemPacoteDTO();
                dto.setIdPacote(1);
                dto.setIdServicoPrestado(1);

                when(pacoteService.obterPorId(1)).thenReturn(Optional.of(new Pacote()));
                when(servicoPrestadoService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(post("/api/item-pacote")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertEquals(
                                                "Serviço Inexistente.",
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()));
        }

        @Test
        void testSalvarItemJaExistente() throws Exception {
                ItemPacoteDTO dto = new ItemPacoteDTO();
                dto.setIdPacote(1);
                dto.setIdServicoPrestado(1);

                ServicoPrestado servico = new ServicoPrestado();
                servico.setDescricao("Serviço Teste");

                when(pacoteService.obterPorId(1)).thenReturn(Optional.of(new Pacote()));
                when(servicoPrestadoService.obterPorId(1)).thenReturn(Optional.of(servico));
                when(itemPacoteService.obterPorPacoteEServicoPrestado(1, 1)).thenReturn(Optional.of(new ItemPacote()));

                mockMvc.perform(post("/api/item-pacote")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason().contains("já cadastrados")));
        }

        @Test
        void testAtualizarPacoteInexistente() throws Exception {
                ItemPacoteDTO dto = new ItemPacoteDTO();
                dto.setIdPacote(1);
                dto.setIdServicoPrestado(1);

                when(pacoteService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(put("/api/item-pacote/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertEquals("Pacote Inexistente",
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()));
        }

        @Test
        void testAtualizarServicoInexistente() throws Exception {
                ItemPacoteDTO dto = new ItemPacoteDTO();
                dto.setIdPacote(1);
                dto.setIdServicoPrestado(1);

                when(pacoteService.obterPorId(1)).thenReturn(Optional.of(new Pacote()));
                when(servicoPrestadoService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(put("/api/item-pacote/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertEquals(
                                                "Serviço Inexistente.",
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()));
        }

        @Test
        void testAtualizarItemNaoEncontrado() throws Exception {
                ItemPacoteDTO dto = new ItemPacoteDTO();
                dto.setIdPacote(1);
                dto.setIdServicoPrestado(1);

                when(pacoteService.obterPorId(1)).thenReturn(Optional.of(new Pacote()));
                when(servicoPrestadoService.obterPorId(1)).thenReturn(Optional.of(new ServicoPrestado()));
                when(itemPacoteService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(put("/api/item-pacote/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertEquals(
                                                "Item não encontrado.",
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()));
        }

        @Test
        void testDeletarItemNaoEncontrado() throws Exception {
                when(itemPacoteService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/api/item-pacote/1")
                                .with(csrf()))
                                .andExpect(status().isNotFound())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertEquals(
                                                "Item não encontrado.",
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()));
        }

        @Test
        void testDeletarPorPacoteInexistente() throws Exception {
                when(pacoteService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/api/item-pacote/pacote/1/servico/1")
                                .with(csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertEquals("Pacote Inexistente",
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()));
        }

        @Test
        void testDeletarPorServicoInexistente() throws Exception {
                when(pacoteService.obterPorId(1)).thenReturn(Optional.of(new Pacote()));
                when(servicoPrestadoService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/api/item-pacote/pacote/1/servico/1")
                                .with(csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertEquals(
                                                "Serviço Inexistente.",
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()));
        }

        @Test
        void testDeletarPorItemNaoEncontrado() throws Exception {
                when(pacoteService.obterPorId(1)).thenReturn(Optional.of(new Pacote()));
                when(servicoPrestadoService.obterPorId(1)).thenReturn(Optional.of(new ServicoPrestado()));
                when(itemPacoteService.obterPorPacoteEServicoPrestado(1, 1)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/api/item-pacote/pacote/1/servico/1")
                                .with(csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result
                                                .getResolvedException() instanceof org.springframework.web.server.ResponseStatusException))
                                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(
                                                ((org.springframework.web.server.ResponseStatusException) result
                                                                .getResolvedException())
                                                                .getReason()
                                                                .contains("Itens de Pacote e Serviço Prestado não")));
        }
}
