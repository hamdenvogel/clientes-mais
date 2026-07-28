package io.github.hvogel.clientes.rest;

import java.util.List;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.rest.exception.ApiErrors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationErrors(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		List<String> messages = bindingResult.getAllErrors()
				.stream()
				.map(MessageSourceResolvable::getDefaultMessage)
				.toList();
		return new ApiErrors(messages);

	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErrors> handleResponseStatusException(ResponseStatusException ex) {
		String mensagemErro = ex.getReason();
		HttpStatus codigoStatus = HttpStatus.valueOf(ex.getStatusCode().value());
		ApiErrors apiErrors = new ApiErrors(mensagemErro);
		return new ResponseEntity<>(apiErrors, codigoStatus);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiErrors> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		ApiErrors apiErrors = new ApiErrors("Nao foi possivel concluir a operacao porque existem registros vinculados.");
		return new ResponseEntity<>(apiErrors, HttpStatus.CONFLICT);
	}
}
