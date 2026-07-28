package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ServicoPrestadoProjectionDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String descricao = "Serviço de manutenção";
        Integer id = 1;
        String nome = "João Silva";
        String pix = "joao@email.com";

        // Act
        ServicoPrestadoProjectionDTO dto = new ServicoPrestadoProjectionDTO(descricao, id, nome, pix);

        // Assert
        assertNotNull(dto);
        assertEquals(descricao, dto.getDescricao());
        assertEquals(id, dto.getId());
        assertEquals(nome, dto.getNome());
        assertEquals(pix, dto.getPix());
    }

    @Test
    void testSetters() {
        // Arrange
        ServicoPrestadoProjectionDTO dto = new ServicoPrestadoProjectionDTO(null, null, null, null);
        String descricao = "Novo serviço";
        Integer id = 2;
        String nome = "Maria Santos";
        String pix = "maria@email.com";

        // Act
        dto.setDescricao(descricao);
        dto.setId(id);
        dto.setNome(nome);
        dto.setPix(pix);

        // Assert
        assertEquals(descricao, dto.getDescricao());
        assertEquals(id, dto.getId());
        assertEquals(nome, dto.getNome());
        assertEquals(pix, dto.getPix());
    }

    @Test
    void testEqualsWithEqualObjects() {
        // Arrange
        ServicoPrestadoProjectionDTO dto1 = new ServicoPrestadoProjectionDTO("Serviço A", 1, "João", "joao@email.com");
        ServicoPrestadoProjectionDTO dto2 = new ServicoPrestadoProjectionDTO("Serviço A", 1, "João", "joao@email.com");

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1, dto1);
    }

    @Test
    void testEqualsWithDifferentObjects() {
        // Arrange
        ServicoPrestadoProjectionDTO dto1 = new ServicoPrestadoProjectionDTO("Serviço A", 1, "João", "joao@email.com");
        ServicoPrestadoProjectionDTO dto2 = new ServicoPrestadoProjectionDTO("Serviço B", 2, "Maria",
                "maria@email.com");

        // Assert
        assertNotEquals(dto1, dto2);
        assertNotEquals(null, dto1);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void testEqualsWithNullFields() {
        // Arrange
        ServicoPrestadoProjectionDTO dto1 = new ServicoPrestadoProjectionDTO(null, null, null, null);
        ServicoPrestadoProjectionDTO dto2 = new ServicoPrestadoProjectionDTO(null, null, null, null);
        ServicoPrestadoProjectionDTO dto3 = new ServicoPrestadoProjectionDTO("Serviço", 1, "Nome", "pix");

        // Assert
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testEqualsWithPartialNullFields() {
        // Arrange
        ServicoPrestadoProjectionDTO dto1 = new ServicoPrestadoProjectionDTO("Serviço A", null, "João", null);
        ServicoPrestadoProjectionDTO dto2 = new ServicoPrestadoProjectionDTO("Serviço A", 1, "João", "pix");

        // Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testHashCode() {
        // Arrange
        ServicoPrestadoProjectionDTO dto1 = new ServicoPrestadoProjectionDTO("Serviço A", 1, "João", "joao@email.com");
        ServicoPrestadoProjectionDTO dto2 = new ServicoPrestadoProjectionDTO("Serviço A", 1, "João", "joao@email.com");

        // Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testHashCodeWithNullFields() {
        // Arrange
        ServicoPrestadoProjectionDTO dto = new ServicoPrestadoProjectionDTO(null, null, null, null);

        // Act & Assert
        assertDoesNotThrow(dto::hashCode);
    }
}
