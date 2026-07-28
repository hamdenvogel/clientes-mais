package io.github.hvogel.clientes.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.service.ValidadorService;

@RestController
@RequestMapping("/api/validador")
public class ValidadorController {

	private static final String TITULO_INFORMACAO = "Informação";

	private final ValidadorService validadorService;

	public ValidadorController(ValidadorService validadorService) {
		super();
		this.validadorService = validadorService;
	}

	@GetMapping("{valor}")
	public InfoResponseDTO validarInteger(@PathVariable String valor) {
		try {
			validadorService.validarValorInteger(valor);
		} catch (RegraNegocioException e) {
			return InfoResponseDTO.builder()
					.withMensagem("Erro")
					.withTitulo(TITULO_INFORMACAO)
					.build();
		}
		return InfoResponseDTO.builder()
				.withMensagem("Valor Integer validado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

}
