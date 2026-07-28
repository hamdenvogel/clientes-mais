package io.github.hvogel.clientes.rest;

import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.rest.dto.ProdutoDTO;
import io.github.hvogel.clientes.service.ProdutoService;
import io.github.hvogel.clientes.service.TotalProdutosService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import io.github.hvogel.clientes.test.base.BaseControllerTest;

@WebMvcTest(ProdutoController.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class ProdutoControllerTest extends BaseControllerTest {

        @MockBean
        private ProdutoService produtoService;

        @MockBean
        private TotalProdutosService totalProdutosService;

        @Test
        void testSalvar() throws Exception {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setDescricao("Produto Teste");
                dto.setPreco(new BigDecimal("10.00"));

                when(produtoService.salvar(any(Produto.class))).thenReturn(new Produto());

                mockMvc.perform(post("/api/produtos")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated());
        }

        @Test
        void testObterTodos() throws Exception {
                when(produtoService.obterTodos()).thenReturn(Collections.emptyList());

                mockMvc.perform(get("/api/produtos")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void testAtualizar() throws Exception {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setDescricao("Produto Atualizado");
                dto.setPreco(new BigDecimal("20.00"));
                dto.setId(1);

                Produto produto = new Produto();
                produto.setId(1);

                when(produtoService.obterPorId(1)).thenReturn(Optional.of(produto));
                when(produtoService.atualizar(any(Produto.class))).thenReturn(produto);

                mockMvc.perform(put("/api/produtos/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isOk());
        }

        @Test
        void testDeletar() throws Exception {
                Produto produto = new Produto();
                produto.setId(1);
                when(produtoService.obterPorId(1)).thenReturn(Optional.of(produto));

                mockMvc.perform(delete("/api/produtos/1")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testObterPorId() throws Exception {
                Produto produto = new Produto();
                produto.setId(1);
                when(produtoService.obterPorId(1)).thenReturn(Optional.of(produto));

                mockMvc.perform(get("/api/produtos/1")
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void testObterTotalProdutos() throws Exception {
                when(totalProdutosService.obterTotalProdutos()).thenReturn(100L);

                mockMvc.perform(get("/api/produtos/totalProdutos")
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalProdutos").value(100));
        }

        @Test
        void testSpecification() throws Exception {
                when(produtoService.executaCriteria(any(), any()))
                                .thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(post("/api/produtos/pesquisa-avancada-criteria")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                                .andExpect(status().isOk());
        }

        @Test
        void testSalvar_Exception() throws Exception {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setDescricao("Produto Erro");
                dto.setPreco(new BigDecimal("10.00"));

                when(produtoService.salvar(any(Produto.class)))
                                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException(
                                                "Erro salvar"));

                mockMvc.perform(post("/api/produtos")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testAtualizar_Exception() throws Exception {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setId(1);
                dto.setDescricao("Produto Erro");
                dto.setPreco(new BigDecimal("10.00"));

                Produto produto = new Produto();
                produto.setId(1);
                when(produtoService.obterPorId(1)).thenReturn(Optional.of(produto));
                when(produtoService.atualizar(any(Produto.class)))
                                .thenThrow(new io.github.hvogel.clientes.exception.RegraNegocioException(
                                                "Erro atualizar"));

                mockMvc.perform(put("/api/produtos/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testAtualizar_NotFound() throws Exception {
                ProdutoDTO dto = new ProdutoDTO();
                dto.setId(1);
                dto.setDescricao("Produto");
                dto.setPreco(new BigDecimal("10.00"));

                when(produtoService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(put("/api/produtos/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testDeletar_NotFound() throws Exception {
                when(produtoService.obterPorId(1)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/api/produtos/1")
                                .with(csrf()))
                                .andExpect(status().isBadRequest()); // Controller returns BAD_REQUEST for delete not
                                                                     // found
        }

        @Test
        void testList() throws Exception {
                when(produtoService.pesquisarPelaDescricao(any(), any()))
                                .thenReturn(org.springframework.data.domain.Page.empty());
                when(produtoService.recuperarTodos(any())).thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/produtos/pesquisa-avancada")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_ComplexSort() throws Exception {
                when(produtoService.recuperarTodos(any())).thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/produtos/pesquisa-avancada")
                                .param("sort", "descricao,desc")
                                .param("sort", "preco,asc")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testSpecification_ComplexSort() throws Exception {
                when(produtoService.executaCriteria(any(), any()))
                                .thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(post("/api/produtos/pesquisa-avancada-criteria")
                                .param("sort", "descricao,desc")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                                .andExpect(status().isOk());
        }

        @Test
        void testSpecification_SplitSort() throws Exception {
                // Test sort passed as separate parameters (missing branch line 167)
                // sort=descricao&sort=asc -> ["descricao", "asc"]
                when(produtoService.executaCriteria(any(), any()))
                                .thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(post("/api/produtos/pesquisa-avancada-criteria")
                                .param("sort", "descricao")
                                .param("sort", "asc")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                                .andExpect(status().isOk());
        }

        @Test
        void testSpecification_SplitSort_Desc() throws Exception {
                when(produtoService.executaCriteria(any(), any()))
                                .thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(post("/api/produtos/pesquisa-avancada-criteria")
                                .param("sort", "descricao")
                                .param("sort", "desc")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_SplitSort() throws Exception {
                // Test sort passed as separate parameters (missing branch line 207)
                when(produtoService.recuperarTodos(any())).thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/produtos/pesquisa-avancada")
                                .param("sort", "descricao")
                                .param("sort", "asc")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_SplitSort_Desc() throws Exception {
                when(produtoService.recuperarTodos(any())).thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/produtos/pesquisa-avancada")
                                .param("sort", "descricao")
                                .param("sort", "desc")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_Descricao() throws Exception {
                // Test filtering by description
                when(produtoService.pesquisarPelaDescricao(any(), any()))
                                .thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/produtos/pesquisa-avancada")
                                .param("descricao", "Teste")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testObterPorId_NotFound() throws Exception {
                when(produtoService.obterPorId(999)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/produtos/999")
                                .with(csrf()))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testList_EmptyDescricao() throws Exception {
                // Test filtering by empty description, should fallback to recuperarTodos
                when(produtoService.recuperarTodos(any())).thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/produtos/pesquisa-avancada")
                                .param("descricao", "")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }

        @Test
        void testList_SingleSortParam() throws Exception {
                // Test sort format: ?sort=descricao (sort array = ["descricao"])
                // Should default to ASC and not throw IndexOutOfBounds
                when(produtoService.recuperarTodos(any())).thenReturn(org.springframework.data.domain.Page.empty());

                mockMvc.perform(get("/api/produtos/pesquisa-avancada")
                                .param("sort", "descricao")
                                .with(csrf()))
                                .andExpect(status().isOk());
        }
}
