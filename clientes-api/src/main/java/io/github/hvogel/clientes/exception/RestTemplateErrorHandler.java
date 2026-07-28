package io.github.hvogel.clientes.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return (response.getStatusCode().is4xxClientError() ||
				response.getStatusCode().is5xxServerError());
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode().is5xxServerError()) {
			// Handle SERVER_ERROR
			throw new HttpClientErrorException(response.getStatusCode());
		} else if (response.getStatusCode().is4xxClientError() && response.getStatusCode() == HttpStatus.NOT_FOUND) {
			// Handle CLIENT_ERROR
			throw new RegraNegocioException("Erro. Código 404. Não encontrado.");
		}
	}

}
