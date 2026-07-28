package io.github.hvogel.clientes.response.jwt;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void testConstructorAndGettersSetters() {
        JwtResponse response = new JwtResponse("token", 1L, "user", "email", Collections.singletonList("ROLE"));

        assertEquals("token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1L, response.getId());
        assertEquals("user", response.getUsername());
        assertEquals("email", response.getEmail());
        assertEquals("ROLE", response.getRoles().getFirst());

        response.setToken("newToken");
        response.setTokenType("Basic");
        response.setId(2L);
        response.setUsername("newUser");
        response.setEmail("newEmail");

        assertEquals("newToken", response.getToken());
        assertEquals("Basic", response.getTokenType());
        assertEquals(2L, response.getId());
        assertEquals("newUser", response.getUsername());
        assertEquals("newEmail", response.getEmail());
    }
}
