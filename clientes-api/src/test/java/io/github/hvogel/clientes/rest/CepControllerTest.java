package io.github.hvogel.clientes.rest;

import io.github.hvogel.clientes.rest.dto.EnderecoDTO;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.service.CepService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CepController.class)
@WithMockUser
class CepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CepService cepService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testPesquisarCep() throws Exception {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep("12345-678");
        enderecoDTO.setLogradouro("Rua Teste");

        when(cepService.pesquisarCep(anyString())).thenReturn(enderecoDTO);

        mockMvc.perform(get("/api/cep/12345678")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("12345-678"))
                .andExpect(jsonPath("$.logradouro").value("Rua Teste"));
    }
}
