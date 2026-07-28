package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EquipamentoDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        EquipamentoDTO dto = new EquipamentoDTO();
        Long id = 1L;
        String descricao = "Equipamento de teste";
        Integer servicoPrestadoId = 10;
        String marca = "Marca X";
        String modelo = "Modelo Y";
        Integer anoFabricacao = 2020;
        Integer anoModelo = 2021;

        // Act
        dto.setId(id);
        dto.setDescricao(descricao);
        dto.setServicoPrestadoId(servicoPrestadoId);
        dto.setMarca(marca);
        dto.setModelo(modelo);
        dto.setAnoFabricacao(anoFabricacao);
        dto.setAnoModelo(anoModelo);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(descricao, dto.getDescricao());
        assertEquals(servicoPrestadoId, dto.getServicoPrestadoId());
        assertEquals(marca, dto.getMarca());
        assertEquals(modelo, dto.getModelo());
        assertEquals(anoFabricacao, dto.getAnoFabricacao());
        assertEquals(anoModelo, dto.getAnoModelo());
    }


    @Test
    void testEquals() {
        // Arrange
        EquipamentoDTO dto1 = new EquipamentoDTO();
        dto1.setId(1L);
        dto1.setDescricao("Equipamento A");
        dto1.setServicoPrestadoId(10);
        dto1.setMarca("Marca X");
        dto1.setModelo("Modelo Y");
        dto1.setAnoFabricacao(2020);
        dto1.setAnoModelo(2021);

        EquipamentoDTO dto2 = new EquipamentoDTO();
        dto2.setId(1L);
        dto2.setDescricao("Equipamento A");
        dto2.setServicoPrestadoId(10);
        dto2.setMarca("Marca X");
        dto2.setModelo("Modelo Y");
        dto2.setAnoFabricacao(2020);
        dto2.setAnoModelo(2021);

        EquipamentoDTO dto3 = new EquipamentoDTO();
        dto3.setId(2L);
        dto3.setDescricao("Equipamento B");

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
        EquipamentoDTO dto1 = new EquipamentoDTO();
        dto1.setId(1L);
        dto1.setDescricao("Equipamento A");
        dto1.setServicoPrestadoId(10);
        dto1.setMarca("Marca X");
        dto1.setModelo("Modelo Y");
        dto1.setAnoFabricacao(2020);
        dto1.setAnoModelo(2021);

        EquipamentoDTO dto2 = new EquipamentoDTO();
        dto2.setId(1L);
        dto2.setDescricao("Equipamento A");
        dto2.setServicoPrestadoId(10);
        dto2.setMarca("Marca X");
        dto2.setModelo("Modelo Y");
        dto2.setAnoFabricacao(2020);
        dto2.setAnoModelo(2021);

        // Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        EquipamentoDTO dto = new EquipamentoDTO();
        dto.setId(1L);
        dto.setDescricao("Equipamento A");
        dto.setServicoPrestadoId(10);
        dto.setMarca("Marca X");
        dto.setModelo("Modelo Y");
        dto.setAnoFabricacao(2020);
        dto.setAnoModelo(2021);

        // Act
        String result = dto.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("descricao=Equipamento A"));
        assertTrue(result.contains("servicoPrestadoId=10"));
        assertTrue(result.contains("marca=Marca X"));
        assertTrue(result.contains("modelo=Modelo Y"));
        assertTrue(result.contains("anoFabricacao=2020"));
        assertTrue(result.contains("anoModelo=2021"));
    }

    @Test
    void testEqualsWithNullFields() {
        // Arrange
        EquipamentoDTO dto1 = new EquipamentoDTO();
        EquipamentoDTO dto2 = new EquipamentoDTO();

        // Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void testHashCodeWithNullFields() {
        // Arrange
        EquipamentoDTO dto = new EquipamentoDTO();

        // Act & Assert
        assertDoesNotThrow(dto::hashCode);
    }
}
