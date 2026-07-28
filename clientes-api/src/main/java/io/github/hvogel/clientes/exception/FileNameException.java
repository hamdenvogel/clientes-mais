package io.github.hvogel.clientes.exception;

import java.io.Serial;

public class FileNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
	
	public FileNameException(String message) {
		super(message);
	}

	public FileNameException(String message, Throwable cause) {
		super(message, cause);
	}
}
