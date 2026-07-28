package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.github.hvogel.clientes.model.entity.Perfil;
import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.model.entity.Usuario;

class UserDetailsImplTest {

    @Test
    void testBuild() {
        Perfil perfil = new Perfil();
        perfil.setNome(EPerfil.ROLE_USER);

        Usuario usuario = Usuario.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .roles(Set.of(perfil))
                .build();

        UserDetailsImpl userDetails = UserDetailsImpl.build(usuario);

        assertNotNull(userDetails);
        assertEquals(1L, userDetails.getId());
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("test@example.com", userDetails.getEmail());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testEquals() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1", "email1", "pass", Collections.emptyList());
        UserDetailsImpl user2 = new UserDetailsImpl(1L, "user1", "email1", "pass", Collections.emptyList());
        UserDetailsImpl user3 = new UserDetailsImpl(2L, "user2", "email2", "pass", Collections.emptyList());

        assertEquals(user2, user1);
        assertNotEquals(user3, user1);
        assertNotEquals(null, user1);
        assertNotEquals(new Object(), user1);
    }

    @Test
    void testInterfaceMethods() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "user", "email", "pass", Collections.emptyList());

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testHashCode() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1", "email1", "pass", Collections.emptyList());
        UserDetailsImpl user2 = new UserDetailsImpl(1L, "user1", "email1", "pass", Collections.emptyList());

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testGetAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        UserDetailsImpl user = new UserDetailsImpl(1L, "admin", "admin@test.com", "pass",
                Collections.singletonList(authority));

        assertNotNull(user.getAuthorities());
        assertEquals(1, user.getAuthorities().size());
        assertTrue(user.getAuthorities().contains(authority));
    }

    @Test
    void testEquals_SameObject() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "user", "email", "pass", Collections.emptyList());
        assertEquals(user, user);
    }

    @Test
    void testEquals_DifferentClass() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "user", "email", "pass", Collections.emptyList());
        assertNotEquals("not a UserDetailsImpl", user);
    }

    @Test
    void testBuildWithMultipleRoles() {
        Perfil perfil1 = new Perfil();
        perfil1.setNome(EPerfil.ROLE_USER);

        Perfil perfil2 = new Perfil();
        perfil2.setNome(EPerfil.ROLE_ADMIN);

        Usuario usuario = Usuario.builder()
                .id(2L)
                .username("admin")
                .email("admin@example.com")
                .password("adminpass")
                .roles(Set.of(perfil1, perfil2))
                .build();

        UserDetailsImpl userDetails = UserDetailsImpl.build(usuario);

        assertNotNull(userDetails);
        assertEquals(2, userDetails.getAuthorities().size());
    }

    @Test
    void testGetId() {
        UserDetailsImpl user = new UserDetailsImpl(123L, "user", "email", "pass", Collections.emptyList());
        assertEquals(123L, user.getId());
    }

    @Test
    void testGetEmail() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "user", "test@example.com", "pass", Collections.emptyList());
        assertEquals("test@example.com", user.getEmail());
    }
}
