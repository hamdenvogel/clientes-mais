package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.service.impl.ReCaptchaAttemptServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReCaptchaAttemptServiceTest {

    private ReCaptchaAttemptServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReCaptchaAttemptServiceImpl();
        // Reset static counter to ensure test isolation
    }

    @Test
    void testReCaptchaSucceeded() {
        String key = "testKey";
        service.reCaptchaFailed(key);
        service.reCaptchaSucceeded(key);
        assertFalse(service.isBlocked(key));
    }

    @Test
    void testReCaptchaFailed() {
        String key = "testKey";
        service.reCaptchaFailed(key);
        service.reCaptchaFailed(key);
        service.reCaptchaFailed(key);
        service.reCaptchaFailed(key);
        assertTrue(service.isBlocked(key));
    }

    @Test
    void testIsBlocked() {
        String key = "testKey";
        assertFalse(service.isBlocked(key));
        service.reCaptchaFailed(key);
        service.reCaptchaFailed(key);
        service.reCaptchaFailed(key);
        service.reCaptchaFailed(key);
        assertTrue(service.isBlocked(key));
    }

    @Test
    void testReCaptchaDeleteAllEntriesInTheCache() {
        String key = "testKey";
        service.reCaptchaFailed(key);
        service.reCaptchaDeleteAllEntriesInTheCache();
        assertFalse(service.isBlocked(key));
    }

    @Test
    void testReCapthaSizeOfCacheVersusTotalAttempts() {
        String info = service.reCapthaSizeOfCacheVersusTotalAttempts();
        assertEquals("Tentativas: 0/4", info);
    }
}
