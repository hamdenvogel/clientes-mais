package io.github.hvogel.clientes.service;

import io.github.hvogel.clientes.rest.dto.GoogleRecaptchaDTO;

public interface GoogleService {
	GoogleRecaptchaDTO validarToken(String token);
	String obterClientIP();	
	void zerarTentativasMalSucedidas();
	String obterInformacaoNumeroDeTentativasDeAcessoAoCapctha();
	void validarCaptchaPreenchido(String captcha);
}
