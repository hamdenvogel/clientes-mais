package io.github.hvogel.clientes.rest.dto;

import io.github.hvogel.clientes.model.entity.Documento;
import io.github.hvogel.clientes.model.entity.Imagem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImagemDTOTest {

    @Test
    void testConstructorWithImagem() {
        Imagem imagem = mock(Imagem.class);
        when(imagem.getUuid()).thenReturn("uuid-123");
        when(imagem.getFileName()).thenReturn("test.jpg");
        when(imagem.getFileType()).thenReturn("image/jpeg");
        when(imagem.getSize()).thenReturn(1024L);
        when(imagem.getChaveId()).thenReturn(1);
        
        ImagemDTO dto = new ImagemDTO(imagem);
        
        assertEquals("uuid-123", dto.getUuid());
        assertEquals("test.jpg", dto.getFileName());
        assertEquals("image/jpeg", dto.getFileType());
        assertEquals(1024L, dto.getSize());
        assertEquals(1, dto.getChaveId());
    }
    
    @Test
    void testGettersAndSetters() {
        ImagemDTO dto = new ImagemDTO(mock(Imagem.class));
        Documento doc = new Documento();
        
        dto.setUuid("uuid-456");
        dto.setFileName("image.png");
        dto.setFileType("image/png");
        dto.setSize(2048L);
        dto.setChaveId(2);
        dto.setDocumentoId(doc);
        dto.setOriginalFileName("original.png");
        
        assertEquals("uuid-456", dto.getUuid());
        assertEquals("image.png", dto.getFileName());
        assertEquals("image/png", dto.getFileType());
        assertEquals(2048L, dto.getSize());
        assertEquals(2, dto.getChaveId());
        assertEquals(doc, dto.getDocumentoId());
        assertEquals("original.png", dto.getOriginalFileName());
    }
    
    @Test
    void testEquals() {
        Imagem imagem = mock(Imagem.class);
        when(imagem.getUuid()).thenReturn("uuid-img");
        when(imagem.getFileName()).thenReturn("test.jpg");
        when(imagem.getFileType()).thenReturn("image/jpeg");
        when(imagem.getSize()).thenReturn(1024L);
        
        ImagemDTO dto1 = new ImagemDTO(imagem);
        dto1.setUuid("uuid-123");
        
        ImagemDTO dto2 = new ImagemDTO(imagem);
        dto2.setUuid("uuid-123");
        
        // Com @Data, todos os campos devem ser iguais
        assertEquals(dto1, dto2);
        
        // Teste com uuid diferente
        dto2.setUuid("uuid-456");
        assertNotEquals(dto1, dto2);
    }
    
    @Test
    void testHashCode() {
        Imagem imagem = mock(Imagem.class);
        when(imagem.getUuid()).thenReturn("uuid-img");
        when(imagem.getFileName()).thenReturn("test.jpg");
        when(imagem.getFileType()).thenReturn("image/jpeg");
        when(imagem.getSize()).thenReturn(1024L);
        
        ImagemDTO dto1 = new ImagemDTO(imagem);
        dto1.setUuid("uuid-123");
        
        ImagemDTO dto2 = new ImagemDTO(imagem);
        dto2.setUuid("uuid-123");
        
        // Com @Data, hashCode deve ser igual quando todos os campos são iguais
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
    
    @Test
    void testToString() {
        Imagem imagem = mock(Imagem.class);
        when(imagem.getUuid()).thenReturn("uuid-123");
        
        ImagemDTO dto = new ImagemDTO(imagem);
        
        String result = dto.toString();
        assertNotNull(result);
    }
}
