package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReCaptchaAttemptServiceImplTest {

    private ReCaptchaAttemptServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReCaptchaAttemptServiceImpl();
        service.reCaptchaDeleteAllEntriesInTheCache();
    }

    @Test
    void testReCaptchaSucceeded() {
        String key = "127.0.0.1";
        service.reCaptchaFailed(key);
        service.reCaptchaSucceeded(key);
        assertFalse(service.isBlocked(key));
    }

    @Test
    void testReCaptchaFailed() {
        String key = "127.0.0.1";
        service.reCaptchaFailed(key);
        // Not blocked yet (1 attempt)
        assertFalse(service.isBlocked(key));
    }

    @Test
    void testIsBlocked() {
        String key = "127.0.0.1";
        for (int i = 0; i < 4; i++) {
            service.reCaptchaFailed(key);
        }
        assertTrue(service.isBlocked(key));
    }

    @Test
    void testReCaptchaDeleteAllEntriesInTheCache() {
        String key = "127.0.0.1";
        service.reCaptchaFailed(key);
        service.reCaptchaDeleteAllEntriesInTheCache();
        assertFalse(service.isBlocked(key));
    }

    @Test
    void testReCapthaSizeOfCacheVersusTotalAttempts() {
        String info = service.reCapthaSizeOfCacheVersusTotalAttempts();
        assertEquals("Tentativas: 0/4", info);

        service.reCaptchaFailed("key");
        info = service.reCapthaSizeOfCacheVersusTotalAttempts();
        assertEquals("Tentativas: 1/4", info);
    }
}
