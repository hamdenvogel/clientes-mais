package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.entity.FotoPerfil;
import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;
import io.github.hvogel.clientes.security.jwt.JwtUtils;
import io.github.hvogel.clientes.service.ClienteService;
import io.github.hvogel.clientes.service.FotoPerfilService;
import io.github.hvogel.clientes.service.PrestadorService;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;

@WebMvcTest(FotoPerfilController.class)
@WithMockUser
class FotoPerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FotoPerfilService fotoPerfilService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private PrestadorService prestadorService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    private io.github.hvogel.clientes.util.HttpServletReqUtil httpServletReqUtil;

    @Test
    void deveUploadFotoDeCliente() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "foto.jpg", "image/jpeg", "conteudo".getBytes());
        Cliente cliente = new Cliente();
        cliente.setId(1);

        FotoPerfil fotoPerfil = new FotoPerfil();
        fotoPerfil.setId(10);
        fotoPerfil.setOwnerType("CLIENTE");
        fotoPerfil.setOwnerId(1);
        fotoPerfil.setMimeType("image/jpeg");
        fotoPerfil.setData("conteudo".getBytes());

        when(clienteService.obterPorId(1)).thenReturn(Optional.of(cliente));
        when(fotoPerfilService.salvarFoto(anyString(), anyInt(), any())).thenReturn(fotoPerfil);

        mockMvc.perform(multipart("/api/fotos/cliente/1").file(file).with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    void deveObterFotoAtivaDePrestador() throws Exception {
        Prestador prestador = new Prestador();
        prestador.setId(2);

        FotoPerfil fotoPerfil = new FotoPerfil();
        fotoPerfil.setMimeType("image/jpeg");
        fotoPerfil.setData("conteudo".getBytes());

        when(prestadorService.obterPorId(2)).thenReturn(Optional.of(prestador));
        when(fotoPerfilService.obterFotoAtiva("prestador", 2)).thenReturn(Optional.of(fotoPerfil));

        mockMvc.perform(get("/api/fotos/prestador/2").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void deveRemoverFotoAtivaDeCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(3);

        when(clienteService.obterPorId(3)).thenReturn(Optional.of(cliente));

        mockMvc.perform(delete("/api/fotos/cliente/3").with(csrf()))
                .andExpect(status().isOk());
    }
}