package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.context.support.WithMockUser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.model.entity.FotoPerfil;
import io.github.hvogel.clientes.model.entity.Profissao;
import io.github.hvogel.clientes.service.FotoPerfilService;
import io.github.hvogel.clientes.rest.dto.PrestadorDTO;
import io.github.hvogel.clientes.service.PrestadorService;
import io.github.hvogel.clientes.service.ProfissaoService;
import io.github.hvogel.clientes.service.RelatorioService;
import io.github.hvogel.clientes.service.TotalPrestadoresService;

import io.github.hvogel.clientes.test.base.BaseControllerTest;

@WebMvcTest(PrestadorController.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class PrestadorControllerTest extends BaseControllerTest {

        @MockBean
        private PrestadorService prestadorService;

        @MockBean
        private ProfissaoService profissaoService;

        @MockBean
        private TotalPrestadoresService totalPrestadoresService;

        @MockBean
        private FotoPerfilService fotoPerfilService;

        @Test
        void testSalvar() throws Exception {
                PrestadorDTO dto = new PrestadorDTO();
                dto.setNome("Prestador Teste");
                dto.setCpf("529.982.247-25");
                dto.setAvaliacao(5);
                dto.setIdProfissao(1);
                dto.setCaptcha("token");

                Profissao profissao = new Profissao();
                profissao.setId(1);

                when(profissaoService.obterPorId(1)).thenReturn(Optional.of(profissao));
                when(prestadorService.salvar(any(Prestador.class))).thenAnswer(invocation -> {
                        Prestador p = invocation.getArgument(0);
                        p.setId(1);
                        return p;
                });

                mockMvc.perform(post("/api/prestador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void testAtualizar() throws Exception {
                PrestadorDTO dto = new PrestadorDTO();
                dto.setNome("Prestador Updated");
                dto.setCpf("529.982.247-25");
                dto.setAvaliacao(5);
                dto.setIdProfissao(1);
                dto.setCaptcha("token");

                Profissao profissao = new Profissao();
                profissao.setId(1);

                Prestador prestador = new Prestador();
                prestador.setId(1);

                when(profissaoService.obterPorId(1)).thenReturn(Optional.of(profissao));
                when(prestadorService.obterPorId(anyInt())).thenReturn(Optional.of(prestador));
                when(prestadorService.atualizar(any(Prestador.class))).thenReturn(prestador);

                mockMvc.perform(put("/api/prestador/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.mensagem").value("Prestador atualizado com sucesso."));
        }

        @Test
        void testDeletar() throws Exception {
                Prestador prestador = new Prestador();
                prestador.setId(1);

                when(prestadorService.obterPorId(1)).thenReturn(Optional.of(prestador));
                doNothing().when(prestadorService).deletar(any(Prestador.class));

                mockMvc.perform(delete("/api/prestador/1")
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.mensagem").value("Registro excluído com sucesso."));
        }

        @Test
        void testObterTodos() throws Exception {
                when(prestadorService.obterTodos()).thenReturn(Arrays.asList(new Prestador()));

                mockMvc.perform(get("/api/prestador"))
                                .andExpect(status().isOk());
        }

        @Test
        void testAcharPorId() throws Exception {
                Prestador prestador = new Prestador();
                prestador.setId(1);
                when(prestadorService.obterPorId(1)).thenReturn(Optional.of(prestador));

                mockMvc.perform(get("/api/prestador/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void testList() throws Exception {
                Page<Prestador> page = new PageImpl<>(Arrays.asList(new Prestador()));
                when(prestadorService.recuperarTodos(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(get("/api/prestador/pesquisa-paginada"))
                                .andExpect(status().isOk());
        }

        @Test
        void testObterTotalPrestadores() throws Exception {
                when(totalPrestadoresService.obterTotalPrestadores()).thenReturn(10L);

                mockMvc.perform(get("/api/prestador/totalPrestadores"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalPrestadores").value(10));
        }

        @Test
        void testSpecification() throws Exception {
                Page<Prestador> page = new PageImpl<>(Arrays.asList(new Prestador()));
                when(prestadorService.executaCriteria(any(), any(Pageable.class))).thenReturn(page);

                mockMvc.perform(post("/api/prestador/criteria")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                                .andExpect(status().isOk());
        }

        @Test
        void testRelatorioPrestador() throws Exception {
                byte[] pdfContent = "PDF".getBytes();
                when(relatorioService.gerarRelatorioPrestador(any(), any(), any())).thenReturn(pdfContent);

                mockMvc.perform(get("/api/prestador/relatorio")
                                .param("id", "1")
                                .param("inicio", "01/01/2023")
                                .param("fim", "31/01/2023"))
                                .andExpect(status().isOk())
                                .andExpect(content().bytes(pdfContent));
        }

        @Test
        void testUploadFoto() throws Exception {
                Prestador prestador = new Prestador();
                prestador.setId(1);
                when(prestadorService.obterPorId(1)).thenReturn(Optional.of(prestador));

                FotoPerfil fotoPerfil = new FotoPerfil();
                fotoPerfil.setId(10);
                fotoPerfil.setOwnerType("PRESTADOR");
                fotoPerfil.setOwnerId(1);
                fotoPerfil.setMimeType("image/jpeg");
                fotoPerfil.setData("conteudo".getBytes());

                when(fotoPerfilService.salvarFoto(anyString(), anyInt(), any())).thenReturn(fotoPerfil);

                MockMultipartFile file = new MockMultipartFile("file", "foto.jpg", "image/jpeg", "conteudo".getBytes());

                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/api/prestador/1/foto")
                                .file(file)
                                .with(csrf()))
                                .andExpect(status().isCreated());
        }

        @Test
        void testObterFoto() throws Exception {
                Prestador prestador = new Prestador();
                prestador.setId(1);
                when(prestadorService.obterPorId(anyInt())).thenReturn(Optional.of(prestador));

                FotoPerfil fotoPerfil = new FotoPerfil();
                fotoPerfil.setMimeType("image/jpeg");
                fotoPerfil.setData("conteudo".getBytes());
                when(fotoPerfilService.obterFotoAtiva(anyString(), anyInt())).thenReturn(Optional.of(fotoPerfil));

                mockMvc.perform(get("/api/prestador/1/foto"))
                                .andExpect(status().isOk());
        }

        @Test
        void testDeletarFoto() throws Exception {
                Prestador prestador = new Prestador();
                prestador.setId(1);
                when(prestadorService.obterPorId(anyInt())).thenReturn(Optional.of(prestador));

                doNothing().when(fotoPerfilService).deletarFotoAtiva(anyString(), anyInt());

                mockMvc.perform(delete("/api/prestador/1/foto").with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testSalvar_ProfissaoInexistente() throws Exception {
                PrestadorDTO dto = new PrestadorDTO();
                dto.setNome("Prestador Teste");
                dto.setCpf("529.982.247-25");
                dto.setAvaliacao(5);
                dto.setIdProfissao(99);
                dto.setCaptcha("token");

                when(profissaoService.obterPorId(99)).thenReturn(Optional.empty());

                mockMvc.perform(post("/api/prestador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testAtualizar_PrestadorNaoEncontrado() throws Exception {
                PrestadorDTO dto = new PrestadorDTO();
                dto.setIdProfissao(1);
                dto.setCpf("529.982.247-25");
                dto.setNome("Nome");
                dto.setPix("Pix");
                dto.setAvaliacao(5);

                when(profissaoService.obterPorId(1)).thenReturn(Optional.of(new Profissao()));
                when(prestadorService.obterPorId(99)).thenReturn(Optional.empty());

                mockMvc.perform(put("/api/prestador/99")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testDeletar_NaoEncontrado() throws Exception {
                when(prestadorService.obterPorId(99)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/api/prestador/99")
                                .with(csrf()))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testAcharPorId_NaoEncontrado() throws Exception {
                when(prestadorService.obterPorId(99)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/prestador/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testSalvar_Exception() throws Exception {
                PrestadorDTO dto = new PrestadorDTO();
                dto.setIdProfissao(1);
                dto.setCpf("123");
                dto.setNome("Nome");
                dto.setAvaliacao(5);
                dto.setCaptcha("token");

                when(profissaoService.obterPorId(1)).thenReturn(Optional.of(new Profissao()));
                when(prestadorService.salvar(any(Prestador.class)))
                                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException(
                                                "Erro salvar"));

                mockMvc.perform(post("/api/prestador")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testAtualizar_Exception() throws Exception {
                PrestadorDTO dto = new PrestadorDTO();
                dto.setIdProfissao(1);
                dto.setCpf("123");
                dto.setNome("Nome");
                dto.setAvaliacao(5);
                dto.setCaptcha("token");

                when(profissaoService.obterPorId(1)).thenReturn(Optional.of(new Profissao()));
                when(prestadorService.obterPorId(1)).thenReturn(Optional.of(new Prestador()));
                when(prestadorService.atualizar(any(Prestador.class)))
                                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException(
                                                "Erro atualizar"));

                mockMvc.perform(put("/api/prestador/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testRelatorioPrestador_DefaultDates() throws Exception {
                byte[] pdfContent = "PDF".getBytes();
                when(relatorioService.gerarRelatorioPrestador(any(), any(), any())).thenReturn(pdfContent);

                // Test with empty dates to trigger default date logic
                mockMvc.perform(get("/api/prestador/relatorio")
                // params "inicio" and "fim" are default empty strings in controller
                )
                                .andExpect(status().isOk())
                                .andExpect(content().bytes(pdfContent));
        }

        @Test
        void testRelatorioPrestador_InvalidDates() throws Exception {
                // Logic: dataInicio = DateUtils.fromString(inicio);
                // dataFim = DateUtils.fromString(fim, true);

                // Pass valid start date so first line executes.
                // Pass invalid end date so second line executes and throws
                // DateTimeParseException.

                byte[] pdfContent = "PDF".getBytes();
                // relatorioService mock not needed if exception throws before call

                org.assertj.core.api.Assertions.assertThatThrownBy(() -> mockMvc.perform(get("/api/prestador/relatorio")
                                .param("inicio", "01/01/2023")
                                .param("fim", "invalid-date")))
                                .hasCauseInstanceOf(java.time.format.DateTimeParseException.class);
        }

        @Test
        void testSpecification_ComplexSort() throws Exception {
                when(prestadorService.executaCriteria(any(), any(Pageable.class)))
                                .thenReturn(new PageImpl<>(Collections.emptyList()));

                mockMvc.perform(post("/api/prestador/criteria")
                                .param("sort", "nome,desc")
                                .param("sort", "cpf,asc")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_ComplexFilters() throws Exception {
                // Test branch: nome != null
                when(prestadorService.pesquisarPeloNome(anyString(), any(Pageable.class)))
                                .thenReturn(new PageImpl<>(Collections.emptyList()));

                mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                .param("nome", "Teste"))
                                .andExpect(status().isOk());

                // Test branch: idPrestador provided
                Prestador p = new Prestador();
                p.setId(1);
                when(prestadorService.obterPorId(1)).thenReturn(Optional.of(p));
                when(prestadorService.obterPorId(eq(1), any(Pageable.class)))
                                .thenReturn(new PageImpl<>(Collections.singletonList(p)));

                mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                .param("id", "1"))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_IdPrestadorNotFound() throws Exception {
                when(prestadorService.obterPorId(99)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                .param("id", "99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testList_LegacySort() throws Exception {
                // Test sort format: ?sort=nome&sort=desc (sort array = ["nome", "desc"])
                Page<Prestador> page = new PageImpl<>(Collections.emptyList());
                when(prestadorService.recuperarTodos(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                .param("sort", "nome")
                                .param("sort", "desc"))
                                .andExpect(status().isOk());
        }

        @Test
        void testSpecification_LegacySort() throws Exception {
                // Test sort format: ?sort=nome&sort=desc (sort array = ["nome", "desc"])
                Page<Prestador> page = new PageImpl<>(Collections.emptyList());
                when(prestadorService.executaCriteria(any(), any(Pageable.class))).thenReturn(page);

                mockMvc.perform(post("/api/prestador/criteria")
                                .param("sort", "nome")
                                .param("sort", "desc")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                                .andExpect(status().isOk());
        }

        @Test
        void testSpecification_LegacySort_Asc() throws Exception {
                // Test branch: "asc".equals(sort[1]) == true
                Page<Prestador> page = new PageImpl<>(Collections.emptyList());
                when(prestadorService.executaCriteria(any(), any(Pageable.class))).thenReturn(page);

                mockMvc.perform(post("/api/prestador/criteria")
                                .param("sort", "nome")
                                .param("sort", "asc")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_LegacySort_Asc() throws Exception {
                // Test branch: direction = "asc".equals(sort[1]) ? Direction.ASC :
                // Direction.DESC;
                // where condition is true.
                Page<Prestador> page = new PageImpl<>(Collections.emptyList());
                when(prestadorService.recuperarTodos(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                .param("sort", "nome")
                                .param("sort", "asc"))
                                .andExpect(status().isOk());
        }

        @Test
        void testAtualizar_ProfissaoInexistente() throws Exception {
                PrestadorDTO dto = new PrestadorDTO();
                dto.setIdProfissao(99);
                dto.setCpf("529.982.247-25");
                dto.setNome("Nome");
                dto.setPix("Pix");
                dto.setAvaliacao(5);

                when(profissaoService.obterPorId(99)).thenReturn(Optional.empty());

                mockMvc.perform(put("/api/prestador/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound());
        }

        @org.junit.jupiter.params.ParameterizedTest
        @org.junit.jupiter.params.provider.ValueSource(strings = { "nome,desc", "nome,asc" })
        void testList_CommaSort_Parameterized(String sortParam) throws Exception {
                // Test sort format: ?sort=nome,desc and ?sort=nome,asc
                Page<Prestador> page = new PageImpl<>(Collections.emptyList());
                when(prestadorService.recuperarTodos(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                .param("sort", sortParam))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_NomeBlank() throws Exception {
                // Test nome as blank string, should fall through to recuperarTodos
                Page<Prestador> page = new PageImpl<>(Collections.emptyList());
                when(prestadorService.recuperarTodos(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                .param("nome", "   "))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_SingleSortParamWithoutComma() throws Exception {
                // Test sort format: ?sort=nome (sort array = ["nome"])
                // This previously caused IndexOutOfBoundsException if controller manual parsing
                // expected 2 elements
                Page<Prestador> page = new PageImpl<>(Collections.emptyList());
                when(prestadorService.recuperarTodos(any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                        .param("sort", "nome"))
                                        .andExpect(status().isOk());
                });
        }

        @Test
        void testList_EmptyResults() throws Exception {
                when(prestadorService.recuperarTodos(any(org.springframework.data.domain.Pageable.class)))
                                .thenReturn(new org.springframework.data.domain.PageImpl<>(
                                                java.util.Collections.emptyList()));

                mockMvc.perform(get("/api/prestador/pesquisa-paginada"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isEmpty());
        }

        @Test
        void testList_NomeNull() throws Exception {
                // Condition: nome == null && idCliente == null
                when(prestadorService.recuperarTodos(any(org.springframework.data.domain.Pageable.class)))
                                .thenReturn(new org.springframework.data.domain.PageImpl<>(
                                                java.util.Collections.emptyList()));

                mockMvc.perform(get("/api/prestador/pesquisa-paginada")
                                .param("nome", (String) null))
                                .andExpect(status().isOk());
        }
}
