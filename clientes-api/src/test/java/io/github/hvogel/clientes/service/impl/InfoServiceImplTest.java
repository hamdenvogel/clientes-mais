package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.github.hvogel.clientes.rest.dto.InfoDTO;

class InfoServiceImplTest {

    @Test
    void testObterInformacoesAplicacao() {
        InfoServiceImpl service = new InfoServiceImpl();

        ReflectionTestUtils.setField(service, "appName", "Sistema de Clientes");
        ReflectionTestUtils.setField(service, "appVersion", "1.0.0");
        ReflectionTestUtils.setField(service, "appAuthor", "Hamden");

        InfoDTO result = service.obterInformacoesAplicacao();

        assertNotNull(result);
        assertEquals("Sistema de Clientes", result.getNameApp());
        assertEquals("1.0.0", result.getVersionApp());
        assertEquals("Hamden", result.getAuthorApp());
    }

    @Test
    void testObterInformacoesAplicacao_WithDifferentValues() {
        InfoServiceImpl service = new InfoServiceImpl();

        ReflectionTestUtils.setField(service, "appName", "Test Application");
        ReflectionTestUtils.setField(service, "appVersion", "2.5.0");
        ReflectionTestUtils.setField(service, "appAuthor", "Test Author");

        InfoDTO result = service.obterInformacoesAplicacao();

        assertNotNull(result);
        assertEquals("Test Application", result.getNameApp());
        assertEquals("2.5.0", result.getVersionApp());
        assertEquals("Test Author", result.getAuthorApp());
    }

    @Test
    void testObterInformacoesAplicacao_ReturnsNotNull() {
        InfoServiceImpl service = new InfoServiceImpl();

        ReflectionTestUtils.setField(service, "appName", "App");
        ReflectionTestUtils.setField(service, "appVersion", "1.0");
        ReflectionTestUtils.setField(service, "appAuthor", "Author");

        InfoDTO result = service.obterInformacoesAplicacao();

        assertNotNull(result);
        assertNotNull(result.getNameApp());
        assertNotNull(result.getVersionApp());
        assertNotNull(result.getAuthorApp());
    }
}
