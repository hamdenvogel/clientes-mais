package io.github.hvogel.clientes.service.impl;

import java.net.URI;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.exception.RestTemplateErrorHandler;
import io.github.hvogel.clientes.interceptor.RequestResponseLoggingInterceptor;
import io.github.hvogel.clientes.model.entity.Captcha;
import io.github.hvogel.clientes.rest.dto.GoogleRecaptchaDTO;
import io.github.hvogel.clientes.service.GoogleService;
import io.github.hvogel.clientes.service.ReCaptchaAttemptService;
import io.github.hvogel.clientes.service.ValidadorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {

	private final HttpServletRequest request;
	private final ReCaptchaAttemptService reCaptchaAttemptService;
	private final Captcha captcha;
	private final ValidadorService validadorService;

	private static final Logger logger = LoggerFactory.getLogger(GoogleServiceImpl.class);

	protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";

	@Override
	public GoogleRecaptchaDTO validarToken(String token) {

		try {
			validadorService.validarTokenGoogle(token);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		if (reCaptchaAttemptService.isBlocked(obterClientIP())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"O cliente excedeu o número máximo de tentativas malsucedidas.");
		}

		try {
			RestTemplate restTemplate = createRestTemplate();

			URI verifyUri = URI.create(RECAPTCHA_URL_TEMPLATE.formatted(captcha.getSecret(), token,
                                    obterClientIP()));

			GoogleRecaptchaDTO googleResponse = restTemplate.getForObject(verifyUri,
					GoogleRecaptchaDTO.class);

			if (googleResponse == null) {
				throw new RegraNegocioException("Erro ao validar Captcha: Resposta nula do Google.");
			}

			logger.info("Google's response: {} ", googleResponse);

			if (!googleResponse.isSuccess()) {
				if (googleResponse.hasClientError()) {
					reCaptchaAttemptService.reCaptchaFailed(obterClientIP());
				}
				throw new RegraNegocioException("Captcha não foi validado." + "\n" +
						"Favor tentar novamente." + " " +
						this.obterInformacaoNumeroDeTentativasDeAcessoAoCapctha());
			}
			reCaptchaAttemptService.reCaptchaSucceeded(obterClientIP());
			return googleResponse;
		} catch (RestClientException e) {
			throw new RegraNegocioException("Erro ao validar o captcha " + e.getMessage() + "\n" +
					"Favor tentar novamente.");
		}
	}

	@Override
	public String obterClientIP() {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}

	@Override
	public void zerarTentativasMalSucedidas() {
		reCaptchaAttemptService.reCaptchaDeleteAllEntriesInTheCache();
	}

	@Override
	public String obterInformacaoNumeroDeTentativasDeAcessoAoCapctha() {
		return reCaptchaAttemptService.reCapthaSizeOfCacheVersusTotalAttempts();
	}

	@Override
	public void validarCaptchaPreenchido(String captcha) {
		if (captcha == null || captcha.isEmpty()) {
			throw new RegraNegocioException("É necessário validar o Captcha!");
		}
	}

	protected RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		restTemplate.setErrorHandler(new RestTemplateErrorHandler());
		return restTemplate;
	}

}
