package io.github.hvogel.clientes.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.rest.dto.GoogleRecaptchaDTO;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.service.GoogleService;

@RestController
@RequestMapping("/api/google")
public class GoogleController {

	private static final String TITULO_INFORMACAO = "Informação";

	private final GoogleService googleService;

	public GoogleController(GoogleService googleService) {
		super();
		this.googleService = googleService;
	}

	@PostMapping("{token}")
	public GoogleRecaptchaDTO validarToken(@PathVariable("token") String token) {

		try {
			return googleService.validarToken(token);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerTentativas() {
		try {
			googleService.zerarTentativasMalSucedidas();
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("informa-validacao")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO informarValidacao() {
		return InfoResponseDTO.builder()
				.withMensagem("Favor validar o Captcha!")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

}
