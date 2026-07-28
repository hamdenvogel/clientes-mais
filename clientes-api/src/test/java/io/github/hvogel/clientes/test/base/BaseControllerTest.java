package io.github.hvogel.clientes.test.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.service.RelatorioService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.util.HttpServletReqUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected RelatorioService relatorioService;

    @MockBean
    protected HttpServletReqUtil reqUtil;

    @MockBean
    protected JwtUtils jwtUtils;

    @MockBean
    protected UserDetailsServiceImpl userDetailsService;

    @MockBean
    protected AuthEntryPointJwt unauthorizedHandler;

}
