package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InfoDTOTest {

    @Test
    void testBuilder() {
        // Arrange
        String nameApp = "Sistema de Clientes";
        String versionApp = "1.0.0";
        String authorApp = "Author Name";

        // Act
        InfoDTO dto = InfoDTO.builder()
                .withNameApp(nameApp)
                .withVersionApp(versionApp)
                .withAuthorApp(authorApp)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(nameApp, dto.getNameApp());
        assertEquals(versionApp, dto.getVersionApp());
        assertEquals(authorApp, dto.getAuthorApp());
    }

    @Test
    void testBuilderWithNullValues() {
        // Act
        InfoDTO dto = InfoDTO.builder().build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getNameApp());
        assertNull(dto.getVersionApp());
        assertNull(dto.getAuthorApp());
    }

    @Test
    void testSetters() {
        // Arrange
        InfoDTO dto = InfoDTO.builder().build();
        String nameApp = "Novo Sistema";
        String versionApp = "2.0.0";
        String authorApp = "Novo Autor";

        // Act
        dto.setNameApp(nameApp);
        dto.setVersionApp(versionApp);
        dto.setAuthorApp(authorApp);

        // Assert
        assertEquals(nameApp, dto.getNameApp());
        assertEquals(versionApp, dto.getVersionApp());
        assertEquals(authorApp, dto.getAuthorApp());
    }

    @Test
    void testEquals() {
        // Arrange
        InfoDTO dto1 = InfoDTO.builder()
                .withNameApp("Sistema A")
                .withVersionApp("1.0.0")
                .withAuthorApp("Author A")
                .build();

        InfoDTO dto2 = InfoDTO.builder()
                .withNameApp("Sistema A")
                .withVersionApp("1.0.0")
                .withAuthorApp("Author A")
                .build();

        InfoDTO dto3 = InfoDTO.builder()
                .withNameApp("Sistema B")
                .withVersionApp("2.0.0")
                .withAuthorApp("Author B")
                .build();

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1, dto1);
        assertNotEquals(dto1, dto3);
        assertNotEquals(null, dto1);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void testHashCode() {
        // Arrange
        InfoDTO dto1 = InfoDTO.builder()
                .withNameApp("Sistema A")
                .withVersionApp("1.0.0")
                .withAuthorApp("Author A")
                .build();

        InfoDTO dto2 = InfoDTO.builder()
                .withNameApp("Sistema A")
                .withVersionApp("1.0.0")
                .withAuthorApp("Author A")
                .build();

        // Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        InfoDTO dto = InfoDTO.builder()
                .withNameApp("Sistema Teste")
                .withVersionApp("3.0.0")
                .withAuthorApp("Test Author")
                .build();

        // Act
        String result = dto.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("nameApp=Sistema Teste"));
        assertTrue(result.contains("versionApp=3.0.0"));
        assertTrue(result.contains("authorApp=Test Author"));
    }

    @Test
    void testEqualsWithNullFields() {
        // Arrange
        InfoDTO dto1 = InfoDTO.builder().build();
        InfoDTO dto2 = InfoDTO.builder().build();

        // Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void testHashCodeWithNullFields() {
        // Arrange
        InfoDTO dto = InfoDTO.builder().build();

        // Act & Assert
        assertDoesNotThrow(dto::hashCode);
    }
}
