package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.exception.SenhaInvalidaException;
import io.github.hvogel.clientes.model.entity.Perfil;
import io.github.hvogel.clientes.model.entity.Usuario;
import io.github.hvogel.clientes.model.repository.PerfilRepository;
import io.github.hvogel.clientes.model.repository.UsuarioRepository;
import io.github.hvogel.clientes.util.Messages;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void testSalvar_SuccessWithDefaultRoleAndEncodedPassword() {
        Usuario usuario = Usuario.builder().username("user").email("user@test.com").cpf("11122233344").password("pass")
            .build();
        Perfil roleUser = new Perfil(EPerfil.ROLE_USER);

        when(repository.findByUsername("user")).thenReturn(Optional.empty());
        when(repository.findByEmail("user@test.com")).thenReturn(Optional.empty());
        when(repository.findByCpf("11122233344")).thenReturn(Optional.empty());
        when(encoder.encode("pass")).thenReturn("encoded-pass");
        when(perfilRepository.findByNome(EPerfil.ROLE_USER)).thenReturn(Optional.of(roleUser));
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario saved = service.salvar(usuario);
        assertNotNull(saved);
        assertEquals("encoded-pass", saved.getPassword());
        assertEquals(1, saved.getRoles().size());
        assertTrue(saved.getRoles().stream().anyMatch(r -> r.getNome() == EPerfil.ROLE_USER));
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void testSalvar_UserExists() {
        Usuario usuario = Usuario.builder().username("user").cpf("11122233344").password("pass").build();
        when(repository.findByUsername("user")).thenReturn(Optional.of(Usuario.builder().id(10L).username("user").build()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(usuario));
    }

    @Test
    void testSalvar_EmailExists() {
        Usuario usuario = Usuario.builder().username("user").email("user@test.com").cpf("11122233344").password("pass")
                .build();
        when(repository.findByUsername("user")).thenReturn(Optional.empty());
        when(repository.findByEmail("user@test.com")).thenReturn(Optional.of(Usuario.builder().id(12L).email("user@test.com").build()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.salvar(usuario));
        assertTrue(ex.getReason().contains("email"));
    }

    @Test
    void testSalvar_ThrowsWhenPasswordIsBlank() {
        Usuario usuario = Usuario.builder().username("user").cpf("11122233344").password("   ").build();
        when(repository.findByUsername("user")).thenReturn(Optional.empty());
        when(repository.findByCpf("11122233344")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.salvar(usuario));
        assertEquals("Senha e obrigatoria.", ex.getReason());
    }

    @Test
    void testAtualizar_WithoutPassword_KeepCurrentPassword() {
        Usuario existente = Usuario.builder().id(1L).username("user-old").email("old@test.com").cpf("12312312312")
            .password("current").build();
        Usuario dados = Usuario.builder().username("user-new").email("new@test.com").cpf("99988877766").password(" ")
            .emailConfirmed(true).build();
        Perfil roleAdmin = new Perfil(EPerfil.ROLE_ADMIN);
        dados.setRoles(Set.of(roleAdmin));

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.findByUsername("user-new")).thenReturn(Optional.empty());
        when(repository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(repository.findByCpf("99988877766")).thenReturn(Optional.empty());
        when(perfilRepository.findByNome(EPerfil.ROLE_ADMIN)).thenReturn(Optional.of(roleAdmin));
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario updated = service.atualizar(1L, dados);

        assertEquals("current", updated.getPassword());
        assertEquals("user-new", updated.getUsername());
        assertTrue(updated.isEmailConfirmed());
        assertTrue(updated.getRoles().stream().anyMatch(r -> r.getNome() == EPerfil.ROLE_ADMIN));
        verify(encoder, never()).encode(any());
    }

    @Test
    void testAtualizar_WithPassword_EncodeAndReplace() {
        Usuario existente = Usuario.builder().id(2L).username("user").email("u@test.com").cpf("11111111111")
            .password("old-enc").build();
        Usuario dados = Usuario.builder().username("user").email("u@test.com").cpf("11111111111").password("nova-senha")
            .build();
        Perfil roleUser = new Perfil(EPerfil.ROLE_USER);

        when(repository.findById(2L)).thenReturn(Optional.of(existente));
        when(repository.findByUsername("user")).thenReturn(Optional.of(existente));
        when(repository.findByEmail("u@test.com")).thenReturn(Optional.of(existente));
        when(repository.findByCpf("11111111111")).thenReturn(Optional.of(existente));
        when(perfilRepository.findByNome(EPerfil.ROLE_USER)).thenReturn(Optional.of(roleUser));
        when(encoder.encode("nova-senha")).thenReturn("nova-enc");
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario updated = service.atualizar(2L, dados);

        assertEquals("nova-enc", updated.getPassword());
        verify(encoder).encode("nova-senha");
    }

    @Test
    void testDeletar_Success() {
        Usuario existente = Usuario.builder().id(3L).username("user").build();
        when(repository.findById(3L)).thenReturn(Optional.of(existente));
        doNothing().when(repository).delete(existente);

        service.deletar(3L);

        verify(repository).delete(existente);
    }

    @Test
    void testObterPorId_NotFound() {
        when(repository.findById(404L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.obterPorId(404L));
        assertEquals(Messages.USUARIO_NAO_ENCONTRADO, ex.getReason());
    }

    @Test
    void testAutenticar_Success() {
        Usuario usuario = Usuario.builder().username("user").password("pass").build();
        Usuario storedUser = Usuario.builder().username("user").password("encodedPass").build();

        when(repository.findByUsername("user")).thenReturn(Optional.of(storedUser));
        when(encoder.matches("pass", "encodedPass")).thenReturn(true);

        UserDetails userDetails = service.autenticar(usuario);
        assertNotNull(userDetails);
        assertEquals("user", userDetails.getUsername());
    }

    @Test
    void testAutenticar_SenhaInvalida() {
        Usuario usuario = Usuario.builder().username("user").password("pass").build();
        Usuario storedUser = Usuario.builder().username("user").password("encodedPass").build();

        when(repository.findByUsername("user")).thenReturn(Optional.of(storedUser));
        when(encoder.matches("pass", "encodedPass")).thenReturn(false);

        assertThrows(SenhaInvalidaException.class, () -> service.autenticar(usuario));
    }

    @Test
    void testLoadUserByUsername_Success() {
        Usuario storedUser = Usuario.builder().username("user").password("encodedPass").build();
        when(repository.findByUsername("user")).thenReturn(Optional.of(storedUser));

        UserDetails userDetails = service.loadUserByUsername("user");
        assertNotNull(userDetails);
        assertEquals("user", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(repository.findByUsername("unknown")).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown"));
        assertEquals("Login não encontrado.", ex.getMessage());
    }
}
