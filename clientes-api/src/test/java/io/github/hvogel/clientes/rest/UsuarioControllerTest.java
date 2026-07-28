package io.github.hvogel.clientes.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.model.entity.Perfil;
import io.github.hvogel.clientes.model.entity.Usuario;
import io.github.hvogel.clientes.rest.dto.UsuarioDTO;
import io.github.hvogel.clientes.service.TotalUsuariosService;
import io.github.hvogel.clientes.service.impl.UsuarioService;
import io.github.hvogel.clientes.test.base.BaseControllerTest;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest extends BaseControllerTest {

    @MockBean
    private UsuarioService service;

    @MockBean
    private TotalUsuariosService totalUsuariosService;

    @Test
    void testSalvar() throws Exception {
    Perfil roleUser = new Perfil(EPerfil.ROLE_USER);
    Usuario usuario = Usuario.builder()
        .id(1L)
        .username("user")
        .email("user@test.com")
        .cpf("12345678901")
        .emailConfirmed(true)
        .roles(Set.of(roleUser))
        .build();

    UsuarioDTO request = new UsuarioDTO();
    request.setUsername("user");
    request.setEmail("user@test.com");
    request.setCpf("12345678901");
    request.setPassword("password123");
    request.setRoles(List.of("ROLE_USER"));

    when(service.salvar(any(Usuario.class))).thenReturn(usuario);

    mockMvc.perform(post("/api/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value("user"))
        .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
        .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testObterTodos() throws Exception {
    Perfil roleAdmin = new Perfil(EPerfil.ROLE_ADMIN);
    Usuario usuario = Usuario.builder()
        .id(10L)
        .username("admin")
        .email("admin@test.com")
        .cpf("12345678902")
        .emailConfirmed(true)
        .roles(Set.of(roleAdmin))
        .build();
    when(service.obterTodos()).thenReturn(List.of(usuario));

    mockMvc.perform(get("/api/usuarios").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(10))
        .andExpect(jsonPath("$[0].username").value("admin"))
        .andExpect(jsonPath("$[0].roles[0]").value("ROLE_ADMIN"));
    }

    @Test
    void testAcharPorId() throws Exception {
    Perfil roleUser = new Perfil(EPerfil.ROLE_USER);
    Usuario usuario = Usuario.builder()
        .id(7L)
        .username("user7")
        .email("u7@test.com")
        .cpf("12345678903")
        .emailConfirmed(false)
        .roles(Set.of(roleUser))
        .build();
    when(service.obterPorId(7L)).thenReturn(usuario);

    mockMvc.perform(get("/api/usuarios/7").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(7))
        .andExpect(jsonPath("$.username").value("user7"));
    }

    @Test
    void testAtualizar() throws Exception {
    UsuarioDTO request = new UsuarioDTO();
    request.setUsername("user-edit");
    request.setEmail("edit@test.com");
    request.setCpf("12345678904");
    request.setPassword("");
    request.setRoles(List.of("ROLE_USER", "ROLE_MODERATOR"));

    when(service.atualizar(eq(5L), any(Usuario.class)))
        .thenReturn(Usuario.builder().id(5L).username("user-edit").build());

    mockMvc.perform(put("/api/usuarios/5")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.titulo").value("Informação"))
        .andExpect(jsonPath("$.mensagem").value("Usuario atualizado com sucesso."));
    }

    @Test
    void testDeletar() throws Exception {
    doNothing().when(service).deletar(9L);

    mockMvc.perform(delete("/api/usuarios/9").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.titulo").value("Informação"))
        .andExpect(jsonPath("$.mensagem").value("Usuario deletado com sucesso."));
    }

    @Test
    void testListarPerfis() throws Exception {
    mockMvc.perform(get("/api/usuarios/perfis").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").value("ROLE_USER"))
        .andExpect(jsonPath("$[1]").value("ROLE_MODERATOR"))
        .andExpect(jsonPath("$[2]").value("ROLE_ADMIN"));
    }

    @Test
    void testSalvarPerfilInvalidoRetorna400() throws Exception {
    UsuarioDTO request = new UsuarioDTO();
    request.setUsername("user");
    request.setCpf("12345678905");
    request.setPassword("password123");
    request.setRoles(List.of("ROLE_INEXISTENTE"));

    mockMvc.perform(post("/api/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0]").value("Perfil invalido: ROLE_INEXISTENTE"));
    }

    @Test
    void testObterTotalUsuarios() throws Exception {
    when(totalUsuariosService.obterTotalUsuarios()).thenReturn(100L);

    mockMvc.perform(get("/api/usuarios/totalUsuarios")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalUsuarios").value(100));
    }
}
