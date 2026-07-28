package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ClienteDTOTest {

    @Test
    void testGettersAndSetters() {
        ClienteDTO dto = new ClienteDTO();

        dto.setId(1);
        dto.setNome("João Silva");
        dto.setCpf("529.982.247-25");
        dto.setDataCadastro(LocalDate.of(2024, 1, 1));
        dto.setPix("joao@example.com");
        dto.setCep("12345-678");
        dto.setEndereco("Rua Teste");
        dto.setComplemento("Apto 101");
        dto.setUf("SP");
        dto.setCidade("São Paulo");

        InfoResponseDTO infoDTO = InfoResponseDTO.builder().build();
        dto.setInfoResponseDTO(infoDTO);

        assertEquals(1, dto.getId());
        assertEquals("João Silva", dto.getNome());
        assertEquals("529.982.247-25", dto.getCpf());
        assertEquals(LocalDate.of(2024, 1, 1), dto.getDataCadastro());
        assertEquals("joao@example.com", dto.getPix());
        assertEquals("12345-678", dto.getCep());
        assertEquals("Rua Teste", dto.getEndereco());
        assertEquals("Apto 101", dto.getComplemento());
        assertEquals("SP", dto.getUf());
        assertEquals("São Paulo", dto.getCidade());
        assertEquals(infoDTO, dto.getInfoResponseDTO());
    }

    @Test
    void testEquals() {
        ClienteDTO dto1 = new ClienteDTO();
        dto1.setId(1);
        dto1.setNome("João");
        dto1.setCpf("529.982.247-25");

        ClienteDTO dto2 = new ClienteDTO();
        dto2.setId(1);
        dto2.setNome("João");
        dto2.setCpf("529.982.247-25");

        ClienteDTO dto3 = new ClienteDTO();
        dto3.setId(2);
        dto3.setNome("Maria");
        dto3.setCpf("123.456.789-00");

        assertEquals(dto2, dto1);
        assertNotEquals(dto3, dto1);
        assertNotEquals(null, dto1);
        assertNotEquals(new Object(), dto1);
        assertEquals(dto1, dto1);
    }

    @Test
    void testHashCode() {
        ClienteDTO dto1 = new ClienteDTO();
        dto1.setId(1);
        dto1.setNome("João");

        ClienteDTO dto2 = new ClienteDTO();
        dto2.setId(1);
        dto2.setNome("João");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(1);
        dto.setNome("João");
        dto.setCpf("529.982.247-25");

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("João"));
        assertTrue(result.contains("529.982.247-25"));
        assertTrue(result.contains("ClienteDTO"));
    }

    @Test
    void testPrePersist() {
        ClienteDTO dto = new ClienteDTO();
        assertNull(dto.getDataCadastro());

        dto.prePersist();

        assertNotNull(dto.getDataCadastro());
        assertEquals(LocalDate.now(), dto.getDataCadastro());
    }
}
