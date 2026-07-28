package io.github.hvogel.clientes.rest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.github.hvogel.clientes.service.DocService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;

@WebMvcTest(DocController.class)
@AutoConfigureMockMvc(addFilters = false)
class DocControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocService docService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void testAddBookmarks() throws Exception {
        doNothing().when(docService).addBookmarks();

        mockMvc.perform(get("/api/doc/add-bookmarks"))
                .andExpect(status().isOk());
    }

    @Test
    void testReplaceBookmark() throws Exception {
        doNothing().when(docService).replaceBookmark();

        mockMvc.perform(get("/api/doc/replace-bookmark"))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveBookmark() throws Exception {
        doNothing().when(docService).removeBookmark();

        mockMvc.perform(get("/api/doc/remove-bookmark"))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeHTML() throws Exception {
        doNothing().when(docService).changeHTML();

        mockMvc.perform(get("/api/doc/change-html"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoadHTML() throws Exception {
        when(docService.loadHTML()).thenReturn(new byte[] { 1, 2, 3 });

        mockMvc.perform(get("/api/doc/load-html"))
                .andExpect(status().isOk());
    }
}
