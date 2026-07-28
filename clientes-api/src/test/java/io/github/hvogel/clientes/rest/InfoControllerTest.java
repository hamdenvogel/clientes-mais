package io.github.hvogel.clientes.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.github.hvogel.clientes.rest.dto.InfoDTO;
import io.github.hvogel.clientes.service.InfoService;
import io.github.hvogel.clientes.util.HttpServletReqUtil;

@WebMvcTest(InfoController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InfoService infoService;

    @MockBean
    private HttpServletReqUtil reqUtil;

    @MockBean
    private io.github.hvogel.clientes.security.jwt.JwtUtils jwtUtils;

    @MockBean
    private io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl userDetailsService;

    @MockBean
    private io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt unauthorizedHandler;

    @Test
    void testObterInformacoesApp() throws Exception {
        InfoDTO info = InfoDTO.builder()
                .withNameApp("Sistema de Clientes")
                .withVersionApp("1.0.0")
                .withAuthorApp("Hamden")
                .build();

        when(infoService.obterInformacoesAplicacao()).thenReturn(info);

        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versionApp").value("1.0.0"))
                .andExpect(jsonPath("$.nameApp").value("Sistema de Clientes"));
    }
}
