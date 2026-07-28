package io.github.hvogel.clientes.exception;

import java.io.Serial;

public class SenhaInvalidaException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

	public SenhaInvalidaException() {
		super("Senha Inválida");
	}
}
