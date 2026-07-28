package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;

@ExtendWith(MockitoExtension.class)
class DocServiceTest {

    @InjectMocks
    private DocService docService;

    @Test
    void testAddBookmarks() {
        assertDoesNotThrow(() -> docService.addBookmarks());
        File file = new File("AddBookmarks.docx");
        assertTrue(file.exists());
        // Clean up handled in next tests or manually if needed, but here we can keep it
        // for replace/remove tests if flow allows,
        // but unit tests should be independent.
        // Logic in DocService.replaceBookmark() loads "AddBookmarks.docx".
        // So we should ensure it exists before calling those methods.
    }

    @Test
    void testReplaceBookmark() {
        // Ensure the file exists
        docService.addBookmarks();

        assertDoesNotThrow(() -> docService.replaceBookmark());

        File file = new File("ReplaceBookmarkContent.docx");
        assertTrue(file.exists());
        if (file.exists())
            file.delete();
        new File("AddBookmarks.docx").delete();
    }

    @Test
    void testRemoveBookmark() {
        // Ensure the file exists
        docService.addBookmarks();

        assertDoesNotThrow(() -> docService.removeBookmark());

        File file = new File("RemoveBookmarks.docx");
        assertTrue(file.exists());
        if (file.exists())
            file.delete();
        new File("AddBookmarks.docx").delete();
    }

    @Test
    void testChangeHTML() {
        // Create dummy DF1.docx
        new Document().saveToFile("DF1.docx", FileFormat.Docx_2013);

        assertDoesNotThrow(() -> docService.changeHTML());

        File file = new File("savechange2.docx");
        assertTrue(file.exists());

        // Clean up
        new File("DF1.docx").delete();
        if (file.exists())
            file.delete();
    }

    @Test
    void testLoadHTML() {
        // Create dummy Paipa_Modelo.docx
        new Document().saveToFile("Paipa_Modelo.docx", FileFormat.Docx_2013);

        byte[] result = docService.loadHTML();
        assertNotNull(result);
        assertTrue(result.length > 0);

        // Clean up
        new File("Paipa_Modelo.docx").delete();
    }

    @Test
    void testConvertToByteArray() throws IOException {
        Document document = new Document();
        byte[] result = DocService.convertToByteArray(document, FileFormat.Docx_2013);
        assertNotNull(result);
    }
}
