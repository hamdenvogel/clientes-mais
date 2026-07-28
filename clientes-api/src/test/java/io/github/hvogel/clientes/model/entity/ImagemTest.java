package io.github.hvogel.clientes.model.entity;

import io.github.hvogel.clientes.helpers.FileNameHelper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImagemTest {

    @Test
    void testGettersAndSetters() {
        Imagem imagem = new Imagem();

        imagem.setFileName("test.jpg");
        imagem.setFileType("image/jpeg");
        imagem.setSize(1024L);
        imagem.setUuid("uuid-123");
        imagem.setSystemName("system");
        imagem.setData(new byte[] { 1, 2, 3 });
        imagem.setChaveId(100);
        imagem.setOriginalFileName("original.jpg");

        Documento documento = new Documento();
        documento.setId(1);
        imagem.setDocumento(documento);

        assertEquals("test.jpg", imagem.getFileName());
        assertEquals("image/jpeg", imagem.getFileType());
        assertEquals(1024L, imagem.getSize());
        assertEquals("uuid-123", imagem.getUuid());
        assertEquals("system", imagem.getSystemName());
        assertArrayEquals(new byte[] { 1, 2, 3 }, imagem.getData());
        assertEquals(100, imagem.getChaveId());
        assertEquals("original.jpg", imagem.getOriginalFileName());
        assertEquals(1, imagem.getDocumento().getId());
    }

    @Test
    void testBuild() {
        Imagem imagem = Imagem.build();

        assertNotNull(imagem);
        assertNotNull(imagem.getUuid());
        assertNotNull(imagem.getCreatedDate());
        assertNotNull(imagem.getUpdatedDate());
        assertEquals("default", imagem.getCreatedBy());
        assertEquals("default", imagem.getSystemName());
        assertEquals("default", imagem.getUpdatedBy());
        assertTrue(imagem.isStatus());
    }

    @Test
    void testSetFiles() {
        Imagem imagem = new Imagem();
        MultipartFile file = new MockMultipartFile("test", "test.jpg", "image/jpeg", new byte[100]);

        imagem.setFiles(file);

        assertEquals("image/jpeg", imagem.getFileType());
        assertEquals(100, imagem.getSize());
    }

    @Test
    void testBuildImagem() {
        MultipartFile file = new MockMultipartFile("test", "original.jpg", "image/jpeg", new byte[] { 1, 2, 3, 4, 5 });
        FileNameHelper helper = new FileNameHelper();

        Imagem imagem = Imagem.buildImagem(file, helper);

        assertNotNull(imagem);
        assertNotNull(imagem.getFileName());
        assertEquals("image/jpeg", imagem.getFileType());
        assertEquals(5, imagem.getSize());
        assertEquals("original.jpg", imagem.getOriginalFileName());
        assertNotNull(imagem.getData());
    }

    @Test
    void testScaleWithZeroWidthAndHeight() throws IOException {
        Imagem imagem = new Imagem();
        byte[] originalData = new byte[] { 1, 2, 3 };
        imagem.setData(originalData);

        byte[] result = imagem.scale(0, 0);

        assertArrayEquals(originalData, result);
    }

    @Test
    void testEquals() {
        Imagem imagem1 = new Imagem();
        imagem1.setId(1);
        imagem1.setFileName("test.jpg");
        imagem1.setUuid("uuid-1");
        imagem1.setData(new byte[] { 1, 2 });

        Imagem imagem2 = new Imagem();
        imagem2.setId(1);
        imagem2.setFileName("test.jpg");
        imagem2.setUuid("uuid-1");
        imagem2.setData(new byte[] { 1, 2 });

        Imagem imagem3 = new Imagem();
        imagem3.setId(2);
        imagem3.setFileName("other.jpg");
        imagem3.setUuid("uuid-2");
        imagem3.setData(new byte[] { 3, 4 });

        assertEquals(imagem1, imagem2);
        assertNotEquals(imagem1, imagem3);
        assertNotEquals(null, imagem1);
        assertEquals(imagem1, imagem1);
    }

    @Test
    void testHashCode() {
        Imagem imagem1 = new Imagem();
        imagem1.setFileName("test.jpg");
        imagem1.setUuid("uuid-1");

        Imagem imagem2 = new Imagem();
        imagem2.setFileName("test.jpg");
        imagem2.setUuid("uuid-1");

        assertEquals(imagem1.hashCode(), imagem2.hashCode());
    }

    @Test
    void testToString() {
        Imagem imagem = new Imagem();
        imagem.setFileName("test.jpg");
        imagem.setUuid("uuid-123");

        String result = imagem.toString();

        assertNotNull(result);
        assertTrue(result.contains("Imagem"));
        assertTrue(result.contains("test.jpg"));
        assertTrue(result.contains("uuid-123"));
    }
}
