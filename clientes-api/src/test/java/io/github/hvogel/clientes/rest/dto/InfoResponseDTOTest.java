package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InfoResponseDTOTest {

        @Test
        void testBuilder() {
                InfoResponseDTO dto = InfoResponseDTO.builder()
                                .withTitulo("Título Teste")
                                .withMensagem("Mensagem Teste")
                                .build();

                assertEquals("Título Teste", dto.getTitulo());
                assertEquals("Mensagem Teste", dto.getMensagem());
        }

        @Test
        void testGettersAndSetters() {
                InfoResponseDTO dto = InfoResponseDTO.builder().build();

                dto.setTitulo("Título");
                dto.setMensagem("Mensagem");

                assertEquals("Título", dto.getTitulo());
                assertEquals("Mensagem", dto.getMensagem());
        }

        @Test
        void testEquals() {
                InfoResponseDTO dto1 = InfoResponseDTO.builder()
                                .withTitulo("Título")
                                .withMensagem("Mensagem")
                                .build();

                InfoResponseDTO dto2 = InfoResponseDTO.builder()
                                .withTitulo("Título")
                                .withMensagem("Mensagem")
                                .build();

                InfoResponseDTO dto3 = InfoResponseDTO.builder()
                                .withTitulo("Outro")
                                .withMensagem("Outra")
                                .build();

                assertEquals(dto1, dto2);
                assertNotEquals(dto1, dto3);
                assertEquals(dto1, dto1);
        }

        @Test
        void testHashCode() {
                InfoResponseDTO dto1 = InfoResponseDTO.builder()
                                .withTitulo("Título")
                                .withMensagem("Mensagem")
                                .build();

                InfoResponseDTO dto2 = InfoResponseDTO.builder()
                                .withTitulo("Título")
                                .withMensagem("Mensagem")
                                .build();

                assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        void testToString() {
                InfoResponseDTO dto = InfoResponseDTO.builder()
                                .withTitulo("Título")
                                .withMensagem("Mensagem")
                                .build();

                String result = dto.toString();

                assertNotNull(result);
                assertTrue(result.contains("Título"));
                assertTrue(result.contains("Mensagem"));
        }
}
