package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.hvogel.clientes.enums.StatusServico;

class ServicoPrestadoDTOTest {

    @Test
    void testGettersAndSetters() {
        ServicoPrestadoDTO dto = new ServicoPrestadoDTO();

        dto.setDescricao("Serviço de teste");
        dto.setPreco("100.00");
        dto.setData("01/01/2024");
        dto.setIdCliente(1);
        dto.setStatus(StatusServico.E);
        dto.setCaptcha("captcha123");
        dto.setIdPrestador(2);
        dto.setTipo("Tipo Teste");
        dto.setIdNatureza(1L);
        dto.setIdAtividade(1L);
        dto.setLocalAtendimento("Local Teste");
        dto.setConclusao("Conclusão Teste");

        InfoResponseDTO infoDTO = InfoResponseDTO.builder().build();
        dto.setInfoResponseDTO(infoDTO);

        assertEquals("Serviço de teste", dto.getDescricao());
        assertEquals("100.00", dto.getPreco());
        assertEquals("01/01/2024", dto.getData());
        assertEquals(1, dto.getIdCliente());
        assertEquals(StatusServico.E, dto.getStatus());
        assertEquals("captcha123", dto.getCaptcha());
        assertEquals(2, dto.getIdPrestador());
        assertEquals("Tipo Teste", dto.getTipo());
        assertEquals(1L, dto.getIdNatureza());
        assertEquals(1L, dto.getIdAtividade());
        assertEquals("Local Teste", dto.getLocalAtendimento());
        assertEquals("Conclusão Teste", dto.getConclusao());
        assertEquals(infoDTO, dto.getInfoResponseDTO());
    }

    @Test
    void testEquals() {
        ServicoPrestadoDTO dto1 = new ServicoPrestadoDTO();
        dto1.setDescricao("Serviço");
        dto1.setIdCliente(1);

        ServicoPrestadoDTO dto2 = new ServicoPrestadoDTO();
        dto2.setDescricao("Serviço");
        dto2.setIdCliente(1);

        ServicoPrestadoDTO dto3 = new ServicoPrestadoDTO();
        dto3.setDescricao("Outro Serviço");
        dto3.setIdCliente(2);

        assertEquals(dto2, dto1);
        assertNotEquals(dto3, dto1);
        assertNotEquals(null, dto1);
        assertEquals(dto1, dto1);
    }

    @Test
    void testHashCode() {
        ServicoPrestadoDTO dto1 = new ServicoPrestadoDTO();
        dto1.setDescricao("Serviço");
        dto1.setIdCliente(1);

        ServicoPrestadoDTO dto2 = new ServicoPrestadoDTO();
        dto2.setDescricao("Serviço");
        dto2.setIdCliente(1);

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
        dto.setDescricao("Serviço de teste");
        dto.setPreco("100.00");

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Serviço de teste"));
        assertTrue(result.contains("100.00"));
    }
}
