package io.github.hvogel.clientes.exception;

import java.io.Serial;

public class UsuarioCadastradoException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

	public UsuarioCadastradoException( String login ) {
		super("Usuário já cadastrado para o login " + login);
	}
}
