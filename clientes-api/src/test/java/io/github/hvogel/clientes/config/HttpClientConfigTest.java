package io.github.hvogel.clientes.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpClientConfigTest {

    @InjectMocks
    private HttpClientConfig httpClientConfig;

    @Test
    void testPoolingConnectionManager() {
        PoolingHttpClientConnectionManager manager = httpClientConfig.poolingConnectionManager();
        assertNotNull(manager);
    }

    @Test
    void testHttpClient() {
        CloseableHttpClient client = httpClientConfig.httpClient();
        assertNotNull(client);
    }

    @Test
    void testIdleConnectionMonitor() {
        PoolingHttpClientConnectionManager manager = httpClientConfig.poolingConnectionManager();
        Runnable monitor = httpClientConfig.idleConnectionMonitor(manager);
        assertNotNull(monitor);
        // Execute the run method to ensure coverage
        monitor.run();
    }

    @Test
    void testIdleConnectionMonitorWithNullManager() {
        Runnable monitor = httpClientConfig.idleConnectionMonitor(null);
        assertNotNull(monitor);
        // Execute the run method to ensure coverage for null check
        monitor.run();
    }
}
