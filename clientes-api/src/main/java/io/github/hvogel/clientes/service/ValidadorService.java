package io.github.hvogel.clientes.service;

import java.math.BigDecimal;

public interface ValidadorService {
	void validarData(String data);
	void validarValorNumerico(String valor);
	BigDecimal converter(String value);
	void validarStatus(String status);
	void validarCep(String cep);
	void validarTokenGoogle(String token);
	void validarValorInteger(String valor);
	void validarValorInteger(String valor, int base);
	void validarTipoServico(String tipo);
	void validarTipoPacote(String tipo);
}
