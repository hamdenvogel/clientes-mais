package io.github.hvogel.clientes.model.entity;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import static org.junit.jupiter.api.Assertions.*;

class ImagemExtraTest {

    @Test
    void testScaleActualResizing() throws IOException {
        // Create a small 10x10 red square image data
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        byte[] imageData = baos.toByteArray();

        Imagem imagem = new Imagem();
        imagem.setData(imageData);

        // Scale to 20x20
        byte[] scaledData = imagem.scale(20, 20);

        assertNotNull(scaledData);

        // Verify dimensions of result
        BufferedImage resultImg = ImageIO.read(new ByteArrayInputStream(scaledData));
        assertEquals(20, resultImg.getWidth());
        assertEquals(20, resultImg.getHeight());
    }

    @Test
    void testDefaultImagem() throws IOException {
        Imagem img = Imagem.defaultImagem();
        assertNotNull(img);
        assertEquals("image/jpeg", img.getFileType());
        assertNotNull(img.getData());
    }

    @Test
    void testDefaultImagemWithScale() throws IOException {
        Imagem img = Imagem.defaultImagem(50, 50);
        assertNotNull(img);

        BufferedImage resultImg = ImageIO.read(new ByteArrayInputStream(img.getData()));
        assertEquals(50, resultImg.getWidth());
        assertEquals(50, resultImg.getHeight());
    }
}
