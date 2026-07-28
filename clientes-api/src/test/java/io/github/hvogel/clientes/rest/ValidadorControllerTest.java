package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.service.ValidadorService;
import io.github.hvogel.clientes.util.HttpServletReqUtil;

@WebMvcTest(ValidadorController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class ValidadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidadorService validadorService;

    @MockBean
    private HttpServletReqUtil reqUtil;

    @MockBean
    private io.github.hvogel.clientes.security.jwt.JwtUtils jwtUtils;

    @MockBean
    private io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl userDetailsService;

    @MockBean
    private io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt unauthorizedHandler;

    @Test
    void testValidarInteger_Success() {
        doNothing().when(validadorService).validarValorInteger(anyString());

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> mockMvc.perform(get("/api/validador/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Valor Integer validado com sucesso."))
                .andExpect(jsonPath("$.titulo").value("Informação")));
    }

    @Test
    void testValidarInteger_Error() {
        doThrow(new RegraNegocioException("Valor inválido"))
                .when(validadorService).validarValorInteger(anyString());

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> mockMvc.perform(get("/api/validador/abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Erro"))
                .andExpect(jsonPath("$.titulo").value("Informação")));
    }
}
