package io.github.hvogel.clientes.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import io.github.hvogel.clientes.exception.RestTemplateErrorHandler;

@Configuration
public class RestTemplateConfig {

	@Value("${via-cep.url}")
	private String viaCepUrl;

	@Bean
	public RestTemplate restTemplate(@Autowired CloseableHttpClient httpClient) {
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory(httpClient));
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(viaCepUrl));
		restTemplate.setErrorHandler(new RestTemplateErrorHandler());
		return restTemplate;
	}

	@Bean
	@ConditionalOnMissingBean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory(@Autowired CloseableHttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(httpClient);
		return clientHttpRequestFactory;
	}
}
