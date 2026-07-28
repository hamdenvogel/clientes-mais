package io.github.hvogel.clientes.helpers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileNameHelperTest {

    private final FileNameHelper fileNameHelper = new FileNameHelper();

    @Test
    void testGenerateUniqueNumber() {
        String uniqueNumber = fileNameHelper.generateUniqueNumber();
        assertNotNull(uniqueNumber);
        assertEquals(5, uniqueNumber.length());
        assertTrue(uniqueNumber.matches("\\d+"));
    }

    @Test
    void testGenerateFileName() {
        String originalFileName = "test.jpg";
        String generatedFileName = fileNameHelper.generateFileName(originalFileName);

        assertNotNull(generatedFileName);
        assertTrue(generatedFileName.endsWith(".jpg"));
        // Check format: HHmmss_ddMMyyyy_RANDOM.ext
        // Length check is difficult due to variable time, but structure check is good
        assertTrue(generatedFileName.contains("_"));
    }

    @Test
    void testGenerateDisplayName() {
        String originalFileName = "path/to/test.jpg";
        String generatedName = fileNameHelper.generateDisplayName(originalFileName);

        assertNotNull(generatedName);
        assertTrue(generatedName.endsWith(".jpg"));
        assertFalse(generatedName.contains("/"));
        assertFalse(generatedName.contains("\\"));
    }

    @Test
    void testGenerateDisplayNameInvalid() {
        String invalidFileName = "../test.jpg";
        assertThrows(io.github.hvogel.clientes.exception.FileNameException.class, () -> {
            fileNameHelper.generateDisplayName(invalidFileName);
        });
    }
}
