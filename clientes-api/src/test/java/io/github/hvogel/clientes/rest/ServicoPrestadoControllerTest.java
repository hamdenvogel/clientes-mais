package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
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

import io.github.hvogel.clientes.model.entity.Atividade;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.entity.Natureza;
import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.ServicoPrestadoDTO;
import io.github.hvogel.clientes.rest.dto.ServicoPrestadoProjectionDTO;
import io.github.hvogel.clientes.service.AtividadeService;
import io.github.hvogel.clientes.service.ClienteService;
import io.github.hvogel.clientes.service.ItemPacoteService;
import io.github.hvogel.clientes.service.NaturezaService;
import io.github.hvogel.clientes.service.PrestadorService;
import io.github.hvogel.clientes.service.ServicoPrestadoService;
import io.github.hvogel.clientes.service.TotalServicosService;
import io.github.hvogel.clientes.service.ValidadorService;

import io.github.hvogel.clientes.test.base.BaseControllerTest;

@WebMvcTest(ServicoPrestadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ServicoPrestadoControllerTest extends BaseControllerTest {

        @MockBean
        private ClienteService clienteService;

        @MockBean
        private ServicoPrestadoService service;

        @MockBean
        private TotalServicosService totalServicosService;

        @MockBean
        private ValidadorService validadorService;

        @MockBean
        private PrestadorService prestadorService;

        @MockBean
        private NaturezaService naturezaService;

        @MockBean
        private ItemPacoteService itemPacoteService;

        @MockBean
        private AtividadeService atividadeService;

        @Test
        void testSalvar() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Teste");
                dto.setPreco("100.00");
                dto.setData("01/01/2023");
                dto.setIdCliente(1);
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Teste");
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);

                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(1);

                Cliente cliente = new Cliente();
                cliente.setId(1);
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                when(naturezaService.obterPorId(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(1L)).thenReturn(Optional.of(new Atividade()));

                doNothing().when(service).validarValoresIniciais(anyString(), anyString(), anyString(), anyInt(),
                                anyString(), anyString(), anyString());

                when(service.salvar(any(ServicoPrestado.class))).thenReturn(entity);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isCreated());
                });
        }

        @Test
        void testAtualizar() {
                Integer id = 1;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Atualizado");
                dto.setPreco("150.00");
                dto.setData("01/02/2023");
                dto.setIdCliente(1);
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Atualizado");
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);
                dto.setIdPrestador(1);

                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(id);

                Cliente cliente = new Cliente();
                cliente.setId(1);

                when(service.obterPorId(id)).thenReturn(Optional.of(entity));
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                when(prestadorService.obterPorId(1)).thenReturn(Optional.of(new Prestador()));
                when(naturezaService.findOneById(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(1L)).thenReturn(Optional.of(new Atividade()));
                when(validadorService.converter(anyString())).thenReturn(BigDecimal.valueOf(150.00));
                when(service.atualizar(any(ServicoPrestado.class))).thenReturn(entity);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.mensagem").value("Serviço atualizado com sucesso."));
                });
        }

        @Test
        void testDeletar() {
                Integer id = 1;
                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(id);

                when(service.obterPorId(id)).thenReturn(Optional.of(entity));
                when(itemPacoteService.existsByServicoPrestado(entity)).thenReturn(false);
                doNothing().when(service).deletar(entity);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(delete("/api/servicos-prestados/" + id))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.mensagem").value("Serviço deletado com sucesso."));
                });
        }

        @Test
        void testDeletarServicosDeUmCliente() {
                Integer idCliente = 1;
                Cliente cliente = new Cliente();
                cliente.setId(idCliente);
                cliente.setNome("Cliente Teste");

                when(clienteService.obterPorId(idCliente)).thenReturn(Optional.of(cliente));
                when(service.existsByCliente(cliente)).thenReturn(true);
                doNothing().when(service).deletarPorCliente(idCliente);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(delete("/api/servicos-prestados/deleta-servicos-cliente/" + idCliente))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.mensagem")
                                                        .value("Serviços do cliente Cliente Teste deletados com sucesso."));
                });
        }

        @Test
        void testAcharPorId() {
                Integer id = 1;
                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(id);
                entity.setDescricao("Serviço Encontrado");

                when(service.obterPorId(id)).thenReturn(Optional.of(entity));

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/" + id))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.descricao").value("Serviço Encontrado"));
                });
        }

        @Test
        void testPesquisar() {
                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(1);
                entity.setDescricao("Serviço Teste");

                when(service.pesquisarNomeClienteEMes(anyString(), anyInt()))
                                .thenReturn(Collections.singletonList(entity));

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados")
                                        .param("nome", "Fulano")
                                        .param("mes", "1"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$[0].descricao").value("Serviço Teste"));
                });
        }

        @Test
        void testPesquisarParcialPorDescricao() {
                String descricao = "Teste";
                ServicoPrestado entity = new ServicoPrestado();
                entity.setDescricao("Teste Parcial");

                when(service.pesquisarParcialPorDescricao(descricao))
                                .thenReturn(Collections.singletonList(entity));

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-descricao/" + descricao))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$[0].descricao").value("Teste Parcial"));
                });
        }

        @Test
        void testObterTotalServicos() {
                when(totalServicosService.obterTotalServicos()).thenReturn(10L);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/totalServicos"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.totalServicos").value(10));
                });
        }

        @Test
        void testPesquisarPorCliente() {
                Integer idCliente = 1;
                Cliente cliente = new Cliente();
                cliente.setId(idCliente);
                ServicoPrestado servico = new ServicoPrestado();
                servico.setDescricao("Serviço Cliente");

                when(clienteService.obterPorId(idCliente)).thenReturn(Optional.of(cliente));
                when(service.pesquisarPorCliente(cliente)).thenReturn(Collections.singletonList(servico));

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-cliente/" + idCliente))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$[0].descricao").value("Serviço Cliente"));
                });
        }

        @Test
        void testPesquisaAvancada() {
                ServicoPrestado servico = new ServicoPrestado();
                servico.setDescricao("Avançada");
                PageImpl<ServicoPrestado> page = new PageImpl<>(Collections.singletonList(servico));

                when(service.pesquisaAvancada(any(ServicoPrestado.class), any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("page", "0")
                                        .param("size", "10")
                                        .param("sort", "id,asc")
                                        .param("descricao", "Avançada"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.content[0].descricao").value("Avançada"));
                });
        }

        @Test
        void testObterConsultaListaServicos() {
                ServicoPrestadoProjectionDTO projection = new ServicoPrestadoProjectionDTO("Projection", 1, "Cliente",
                                "Pix");

                when(service.findAllServicoPrestadoProjectionDTO()).thenReturn(Collections.singletonList(projection));

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/consulta-lista-servicos"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$[0].descricao").value("Projection"));
                });
        }

        @Test
        void testRelatorioServicosPrestados() {
                byte[] pdfContent = "PDF Content".getBytes();
                when(relatorioService.gerarRelatorioServicosPrestados(any(), any())).thenReturn(pdfContent);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/relatorio")
                                        .param("inicio", "01/01/2023")
                                        .param("fim", "31/01/2023"))
                                        .andExpect(status().isOk())
                                        .andExpect(content().bytes(pdfContent));
                });
        }

        @Test
        void testSalvar_StatusInvalido() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Teste");
                dto.setData("01/01/2023");
                dto.setPreco("100.00");
                dto.setIdCliente(1);
                // Status null

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testSalvar_ClienteInexistente() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Teste");
                dto.setData("01/01/2023");
                dto.setPreco("100.00");
                dto.setIdCliente(1);
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Teste");
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);

                when(clienteService.obterPorId(1)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testSalvar_PrestadorInexistente() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Teste");
                dto.setData("01/01/2023");
                dto.setPreco("100.00");
                dto.setIdCliente(1);
                dto.setIdPrestador(99); // ID invalido
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Teste");
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);

                when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
                when(prestadorService.obterPorId(99)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testAtualizar_ServicoNaoEncontrado() {
                Integer id = 99;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Atualizado");
                dto.setPreco("150.00");
                dto.setData("01/02/2023");
                dto.setIdCliente(1);
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Teste");
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);

                when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
                when(naturezaService.findOneById(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(1L)).thenReturn(Optional.of(new Atividade()));
                when(service.obterPorId(id)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testDeletar_ServicoNaoEncontrado() {
                Integer id = 99;
                when(service.obterPorId(id)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(delete("/api/servicos-prestados/" + id))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testDeletar_ServicoVinculadoPacote() {
                Integer id = 1;
                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(id);

                when(service.obterPorId(id)).thenReturn(Optional.of(entity));
                when(itemPacoteService.existsByServicoPrestado(entity)).thenReturn(true);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(delete("/api/servicos-prestados/" + id))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testDeletarServicosDeUmCliente_SemServicos() {
                Integer id = 1;
                Cliente cliente = new Cliente();
                cliente.setId(id);

                when(clienteService.obterPorId(id)).thenReturn(Optional.of(cliente));
                when(service.existsByCliente(cliente)).thenReturn(false);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(delete("/api/servicos-prestados/deleta-servicos-cliente/" + id))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testDeletarServicosDeUmCliente_ClienteInexistente() {
                Integer id = 99;
                when(clienteService.obterPorId(id)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(delete("/api/servicos-prestados/deleta-servicos-cliente/" + id))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testList_MultiploSort() {
                ServicoPrestado servico = new ServicoPrestado();
                servico.setDescricao("Paginação Sort");
                PageImpl<ServicoPrestado> page = new PageImpl<>(Collections.singletonList(servico));

                when(service.recuperarTodos(any(Pageable.class))).thenReturn(page);

                // Test parsing of sort=["field,direction", "field2,direction"]
                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-paginada")
                                        .param("page", "0")
                                        .param("size", "10")
                                        .param("sort", "id,asc")
                                        .param("sort", "descricao,desc")
                                        .with(csrf()))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.content[0].descricao").value("Paginação Sort"));
                });
        }

        @Test
        void testList_ClienteNotFound() {
                when(clienteService.obterPorId(anyInt())).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-paginada")
                                        .param("id", "1")
                                        .with(csrf()))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testList_WithCliente() {
                ServicoPrestado servico = new ServicoPrestado();
                servico.setDescricao("Paginação Com Cliente");
                PageImpl<ServicoPrestado> page = new PageImpl<>(Collections.singletonList(servico));

                when(clienteService.obterPorId(anyInt())).thenReturn(Optional.of(new Cliente()));
                when(service.pesquisarPorIdCliente(anyInt(), any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-paginada")
                                        .param("id", "1")
                                        .with(csrf()))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.content[0].descricao").value("Paginação Com Cliente"));
                });
        }

        @Test
        void testPesquisaAvancada_Completa() {
                ServicoPrestado servico = new ServicoPrestado();
                servico.setDescricao("Avançada Completa");
                PageImpl<ServicoPrestado> page = new PageImpl<>(Collections.singletonList(servico));

                when(service.pesquisarPeloNomeDoCliente(anyString(), any(Pageable.class))).thenReturn(page);
                // When "cliente.nome" is provided, it calls pesquisarPeloNomeDoCliente

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("page", "0")
                                        .param("size", "10")
                                        .param("cliente.nome", "Fulano"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.content[0].descricao").value("Avançada Completa"));
                });

                // Test "Orfaos" branch
                when(service.obterServicosAindaNaoVinculados(any(Pageable.class))).thenReturn(page);
                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("orfaos", "true"))
                                        .andExpect(status().isOk());
                });

                // Test "Status" and "Cliente ID" branch
                when(clienteService.obterPorId(anyInt())).thenReturn(Optional.of(new Cliente()));
                when(service.pesquisaAvancada(any(ServicoPrestado.class), any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("cliente", "1")
                                        .param("status", "E"))
                                        .andExpect(status().isOk());
                });
        }

        @Test
        void testSalvar_ComPrestador() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Completo");
                dto.setPreco("100.00");
                dto.setData("01/01/2023");
                dto.setIdCliente(1);
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Teste");
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);
                dto.setIdPrestador(1);

                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(1);

                Cliente cliente = new Cliente();
                cliente.setId(1);
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                when(naturezaService.obterPorId(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(1L)).thenReturn(Optional.of(new Atividade()));
                when(prestadorService.obterPorId(1)).thenReturn(Optional.of(new Prestador()));

                doNothing().when(service).validarValoresIniciais(anyString(), anyString(), anyString(), anyInt(),
                                anyString(), anyString(), anyString());

                when(service.salvar(any(ServicoPrestado.class))).thenReturn(entity);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isCreated());
                });
        }

        @Test
        void testAtualizar_Completo() {
                Integer id = 1;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Atualizado Completo");
                dto.setPreco("150.00");
                dto.setData("01/02/2023");
                dto.setIdCliente(1);
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Atualizado");
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);
                dto.setIdPrestador(1);

                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(id);

                Cliente cliente = new Cliente();
                cliente.setId(1);

                when(service.obterPorId(id)).thenReturn(Optional.of(entity));
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                when(prestadorService.obterPorId(1)).thenReturn(Optional.of(new Prestador()));
                when(naturezaService.findOneById(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(1L)).thenReturn(Optional.of(new Atividade()));
                when(validadorService.converter(anyString())).thenReturn(BigDecimal.valueOf(150.00));

                // Mock atualizar to return entity
                when(service.atualizar(any(ServicoPrestado.class))).thenReturn(entity);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.mensagem").value("Serviço atualizado com sucesso."));
                });
        }

        @Test
        void testSalvar_RegraNegocioException() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Inválido");
                dto.setPreco("100.00");
                dto.setData("01/01/2023");
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Teste"); // Required
                dto.setIdNatureza(1L); // Required
                dto.setIdAtividade(1L); // Required
                dto.setIdCliente(1); // Required

                // Simulate validation error
                doThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro de validação"))
                                .when(service)
                                .validarValoresIniciais(any(), any(), any(), anyInt(), any(), any(), any());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.message").doesNotExist()); // Or check specific error
                                                                                          // structure
                });
                // if needed
        }

        @Test
        void testSalvar_NaturezaNaoEncontrada() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Teste");
                dto.setData("01/01/2023");
                dto.setPreco("100.00");
                dto.setIdCliente(1);
                dto.setIdNatureza(99L); // Invalid ID
                dto.setIdAtividade(1L); // Required
                dto.setTipo("Tipo Teste"); // Required
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);

                Cliente cliente = new Cliente();
                cliente.setId(1);
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                when(naturezaService.obterPorId(99L)).thenReturn(Optional.empty());

                doNothing().when(service).validarValoresIniciais(any(), any(), any(), anyInt(), any(), any(), any());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testSalvar_AtividadeNaoEncontrada() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Teste");
                dto.setData("01/01/2023");
                dto.setPreco("100.00");
                dto.setIdCliente(1);
                dto.setIdAtividade(99L); // Invalid ID
                dto.setIdNatureza(1L); // Required
                dto.setTipo("Tipo Teste"); // Required
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);

                Cliente cliente = new Cliente();
                cliente.setId(1);
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                // Mock Natureza found
                when(naturezaService.obterPorId(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(99L)).thenReturn(Optional.empty());

                doNothing().when(service).validarValoresIniciais(any(), any(), any(), anyInt(), any(), any(), any());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testAtualizar_RegraNegocioException() {
                Integer id = 1;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Atualizado");
                dto.setPreco("150.00");
                dto.setData("01/02/2023");
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setIdCliente(1); // Required
                dto.setIdNatureza(1L); // Required
                dto.setIdAtividade(1L); // Required
                dto.setTipo("Tipo Teste"); // Required

                doThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro de validação"))
                                .when(service).validarValoresIniciaisAlteracao(any(), any(), any(), any(), any());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testAtualizar_NaturezaNaoEncontrada() {
                Integer id = 1;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Atualizado");
                dto.setPreco("150.00");
                dto.setData("01/02/2023");
                dto.setIdCliente(1);
                dto.setIdNatureza(99L);
                dto.setIdAtividade(1L); // Required
                dto.setTipo("Tipo Teste"); // Required
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);

                when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
                when(naturezaService.findOneById(99L)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isBadRequest()); // Controller returns BAD_REQUEST for this
                                                                             // case
                });
        }

        @Test
        void testAtualizar_AtividadeNaoEncontrada() {
                Integer id = 1;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Atualizado");
                dto.setPreco("150.00");
                dto.setData("01/02/2023");
                dto.setIdCliente(1);
                dto.setIdAtividade(99L);
                dto.setIdNatureza(1L); // Required
                dto.setTipo("Tipo Teste"); // Required
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);

                when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
                when(naturezaService.findOneById(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(99L)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isNotFound());
                });
        }

        @Test
        void testPesquisarParcialPorDescricao_Exception() {
                String descricao = "Erro";
                when(service.pesquisarParcialPorDescricao(descricao))
                                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException(
                                                "Erro pesquisa"));

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-descricao/" + descricao))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testPesquisarPorCliente_Exception() {
                Integer id = 1;
                Cliente cliente = new Cliente();
                cliente.setId(id);
                when(clienteService.obterPorId(id)).thenReturn(Optional.of(cliente));
                when(service.pesquisarPorCliente(cliente))
                                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException(
                                                "Erro pesquisa cliente"));

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-cliente/" + id))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testPesquisarParcialPorDescricao_RegraNegocioException() {
                String descricao = "Erro";
                doThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro busca"))
                                .when(service).pesquisarParcialPorDescricao(descricao);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-descricao/" + descricao))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testPesquisarPorCliente_RegraNegocioException() {
                Integer id = 1;
                Cliente cliente = new Cliente();
                cliente.setId(id);
                when(clienteService.obterPorId(id)).thenReturn(Optional.of(cliente));

                doThrow(new io.github.hvogel.clientes.exception.RegraNegocioException("Erro busca"))
                                .when(service).pesquisarPorCliente(cliente);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-cliente/" + id))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testPesquisaAvancada_SemFiltros() {
                // Test branch: nomeCliente == null && orfaos == null
                PageImpl<ServicoPrestado> page = new PageImpl<>(Collections.emptyList());
                when(service.pesquisaAvancada(any(ServicoPrestado.class), any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("page", "0")
                                        .param("size", "10")
                        // No other params
                        )
                                        .andExpect(status().isOk());
                });
        }

        @Test
        void testAtualizar_PrestadorInexistente() {
                Integer id = 1;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Atualizado");
                dto.setPreco("150.00");
                dto.setData("01/02/2023");
                dto.setIdCliente(1);
                dto.setIdPrestador(99); // Invalid
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Teste");
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);

                when(clienteService.obterPorId(1)).thenReturn(Optional.of(new Cliente()));
                when(prestadorService.obterPorId(99)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testSalvar_SemOpcionais() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Minimal");
                dto.setData("01/01/2023");
                dto.setPreco("100.00");
                dto.setIdCliente(1);
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Teste");
                // Required fields
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);

                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(1);

                Cliente cliente = new Cliente();
                cliente.setId(1);
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                when(naturezaService.obterPorId(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(1L)).thenReturn(Optional.of(new Atividade()));

                doNothing().when(service).validarValoresIniciais(anyString(), anyString(), anyString(), anyInt(),
                                anyString(), anyString(), anyString());
                when(service.salvar(any(ServicoPrestado.class))).thenReturn(entity);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isCreated());
                });
        }

        @Test
        void testAtualizar_SemOpcionais() {
                Integer id = 1;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setDescricao("Serviço Atualizado Minimal");
                dto.setPreco("150.00");
                dto.setData("01/02/2023");
                dto.setIdCliente(1);
                dto.setStatus(io.github.hvogel.clientes.enums.StatusServico.E);
                dto.setTipo("Tipo Atualizado");
                // Required fields
                dto.setIdNatureza(1L);
                dto.setIdAtividade(1L);

                ServicoPrestado entity = new ServicoPrestado();
                entity.setId(id);

                Cliente cliente = new Cliente();
                cliente.setId(1);

                when(service.obterPorId(id)).thenReturn(Optional.of(entity));
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                when(naturezaService.findOneById(1L)).thenReturn(Optional.of(new Natureza()));
                when(atividadeService.obterPorId(1L)).thenReturn(Optional.of(new Atividade()));
                when(validadorService.converter(anyString())).thenReturn(BigDecimal.valueOf(150.00));
                when(service.atualizar(any(ServicoPrestado.class))).thenReturn(entity);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isOk());
                });
        }

        @Test
        void testPesquisarPorCliente_ClienteInexistente() {
                Integer id = 99;
                when(clienteService.obterPorId(id)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-cliente/" + id))
                                        .andExpect(status().isBadRequest());
                });
        }

        @org.junit.jupiter.params.ParameterizedTest
        @org.junit.jupiter.params.provider.CsvSource(delimiter = ';', value = {
                        "id,asc;Paginação",
                        "descricao;Paginação Sort Single",
                        "descricao,;Sort Comma Ending"
        })
        void testList_SortingVariations(String sortParam, String expectedDesc) {
                ServicoPrestado servico = new ServicoPrestado();
                servico.setDescricao(expectedDesc.trim());
                PageImpl<ServicoPrestado> page = new PageImpl<>(Collections.singletonList(servico));

                when(service.recuperarTodos(any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-paginada")
                                        .param("page", "0")
                                        .param("size", "10")
                                        .param("sort", sortParam))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.content[0].descricao").value(expectedDesc.trim()));
                });
        }

        @Test
        void testPesquisaAvancada_Orphans() {
                Page<ServicoPrestado> page = new PageImpl<>(Collections.emptyList());
                when(service.obterServicosAindaNaoVinculados(any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("orfaos", "true"))
                                        .andExpect(status().isOk());
                });
        }

        @Test
        void testPesquisaAvancada_ByClientName() {
                Page<ServicoPrestado> page = new PageImpl<>(Collections.emptyList());
                when(service.pesquisarPeloNomeDoCliente(anyString(), any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("cliente.nome", "Test"))
                                        .andExpect(status().isOk());
                });
        }

        @Test
        void testPesquisaAvancada_WithStatusAndClient() {
                Cliente cliente = new Cliente();
                cliente.setId(1);
                when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
                Page<ServicoPrestado> page = new PageImpl<>(Collections.emptyList());
                when(service.pesquisaAvancada(any(ServicoPrestado.class), any(Pageable.class))).thenReturn(page);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("cliente", "1")
                                        .param("status", "E"))
                                        .andExpect(status().isOk());
                });
        }

        @Test
        void testPesquisaAvancada_ClienteInexistente() {
                Integer id = 99;
                when(clienteService.obterPorId(id)).thenReturn(Optional.empty());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/pesquisa-avancada")
                                        .param("cliente", String.valueOf(id)))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testSalvar_StatusNull() {
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setData("01/01/2023");
                dto.setPreco("100.00");
                dto.setStatus(null);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(post("/api/servicos-prestados")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testAtualizar_StatusNull() {
                Integer id = 1;
                ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
                dto.setData("01/01/2023");
                dto.setPreco("100.00");
                dto.setStatus(null);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(put("/api/servicos-prestados/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(dto)))
                                        .andExpect(status().isBadRequest());
                });
        }

        @Test
        void testList_SingleSortNoComma() throws Exception {
                ServicoPrestado servico = new ServicoPrestado();
                servico.setDescricao("Single Sort No Comma");
                PageImpl<ServicoPrestado> page = new PageImpl<>(Collections.singletonList(servico));

                when(service.recuperarTodos(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(get("/api/servicos-prestados/pesquisa-paginada")
                                .param("sort", "id")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testRelatorioServicosPrestados_NullDates() throws Exception {
                byte[] pdfContent = "PDF Content".getBytes();
                when(relatorioService.gerarRelatorioServicosPrestados(any(), any())).thenReturn(pdfContent);

                mockMvc.perform(get("/api/servicos-prestados/relatorio")
                                .param("inicio", "")
                                .param("fim", ""))
                                .andExpect(status().isOk())
                                .andExpect(content().bytes(pdfContent));
        }

        @Test
        void testList_EmptyResults() throws Exception {
                when(service.recuperarTodos(any(org.springframework.data.domain.Pageable.class)))
                                .thenReturn(new org.springframework.data.domain.PageImpl<>(
                                                java.util.Collections.emptyList()));

                mockMvc.perform(get("/api/servicos-prestados/pesquisa-paginada"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isEmpty());
        }

        @Test
        void testList_NomeNull() throws Exception {
                when(service.recuperarTodos(any(org.springframework.data.domain.Pageable.class)))
                                .thenReturn(new org.springframework.data.domain.PageImpl<>(
                                                java.util.Collections.emptyList()));

                mockMvc.perform(get("/api/servicos-prestados/pesquisa-paginada")
                                .param("nome", (String) null))
                                .andExpect(status().isOk());
        }

        @Test
        void testRelatorioServicosPrestados_NullParams() {
                byte[] pdfContent = "PDF Content".getBytes();
                when(relatorioService.gerarRelatorioServicosPrestados(any(), any())).thenReturn(pdfContent);

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados/relatorio")
                                        .param("inicio", "")
                                        .param("fim", "")
                                        .with(csrf()))
                                        .andExpect(status().isOk())
                                        .andExpect(content().bytes(pdfContent));
                });
        }

        @Test
        void testPesquisar_NullMes() {
                when(service.pesquisarNomeClienteEMes(anyString(), any()))
                                .thenReturn(Collections.emptyList());

                org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
                        mockMvc.perform(get("/api/servicos-prestados")
                                        .param("nome", "Teste")
                                        .with(csrf()))
                                        .andExpect(status().isOk());
                });
        }
}
