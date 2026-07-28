package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrestadorDTOTest {

    @Test
    void testGettersAndSetters() {
        PrestadorDTO dto = new PrestadorDTO();

        dto.setId(1);
        dto.setNome("João Prestador");
        dto.setCpf("529.982.247-25");
        dto.setPix("joao@example.com");
        dto.setAvaliacao(5);
        dto.setIdProfissao(1);
        dto.setEmail("joao@example.com");
        dto.setCaptcha("captcha123");

        assertEquals(1, dto.getId());
        assertEquals("João Prestador", dto.getNome());
        assertEquals("529.982.247-25", dto.getCpf());
        assertEquals("joao@example.com", dto.getPix());
        assertEquals(5, dto.getAvaliacao());
        assertEquals(1, dto.getIdProfissao());
        assertEquals("joao@example.com", dto.getEmail());
        assertEquals("captcha123", dto.getCaptcha());
    }

    @Test
    void testEquals() {
        PrestadorDTO dto1 = new PrestadorDTO();
        dto1.setId(1);
        dto1.setNome("João");

        PrestadorDTO dto2 = new PrestadorDTO();
        dto2.setId(1);
        dto2.setNome("João");

        PrestadorDTO dto3 = new PrestadorDTO();
        dto3.setId(2);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        PrestadorDTO dto1 = new PrestadorDTO();
        dto1.setId(1);

        PrestadorDTO dto2 = new PrestadorDTO();
        dto2.setId(1);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PrestadorDTO dto = new PrestadorDTO();
        dto.setNome("João");

        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("João"));
    }
}
