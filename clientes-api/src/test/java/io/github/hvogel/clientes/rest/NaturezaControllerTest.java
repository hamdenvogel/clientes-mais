package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.hvogel.clientes.model.entity.Natureza;
import io.github.hvogel.clientes.rest.dto.NaturezaDTO;
import io.github.hvogel.clientes.service.NaturezaService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(NaturezaController.class)
@AutoConfigureMockMvc(addFilters = false)
class NaturezaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NaturezaService naturezaService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testSave() throws Exception {
        NaturezaDTO dto = new NaturezaDTO();
        dto.setDescricao("Natureza Teste");
        when(naturezaService.save(any(Natureza.class))).thenReturn(new Natureza());

        mockMvc.perform(post("/api/natureza")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}
