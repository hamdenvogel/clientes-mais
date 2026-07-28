package io.github.hvogel.clientes.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RestTemplateConfigTest {

    @InjectMocks
    private RestTemplateConfig restTemplateConfig;

    @Mock
    private CloseableHttpClient httpClient;

    @Test
    void testClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = restTemplateConfig.clientHttpRequestFactory(httpClient);
        assertNotNull(factory);
    }

    @Test
    void testRestTemplate() {
        // Need to simulate @Value injection or set the field via reflection if used in
        // constructor but here it's used in method
        // Using reflection to set private field via_cep_url
        org.springframework.test.util.ReflectionTestUtils.setField(restTemplateConfig, "viaCepUrl",
                "http://viacep.com.br/ws/");

        RestTemplate restTemplate = restTemplateConfig.restTemplate(httpClient);
        assertNotNull(restTemplate);
    }
}
