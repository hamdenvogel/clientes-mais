package io.github.hvogel.clientes.service.impl;

import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.enums.StatusServico;
import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.service.ValidadorService;
import io.github.hvogel.clientes.util.BigDecimalConverter;
import io.github.hvogel.clientes.util.ValidDouble;

import io.github.hvogel.clientes.util.Messages;

@Service
public class ValidadorServiceImpl implements ValidadorService {
	private final ValidDouble validDouble;
	private final BigDecimalConverter bigDecimalConverter;
	protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
	private static final String VALOR_INVALIDO = Messages.VALOR_INVALIDO;

	public enum tipoServico {
		U, // Unitário
		P // Pacote (de serviços)
	}

	public enum tipoPacote {
		I, // Iniciado
		A, // Aguardando atendimento
		E, // Em atendimento
		C, // Cancelado
		F // Finalizado
	}

	public ValidadorServiceImpl(ValidDouble validDouble, BigDecimalConverter bigDecimalConverter) {
		super();
		this.validDouble = validDouble;
		this.bigDecimalConverter = bigDecimalConverter;
	}

	public static String[] montaArrayTipoServicos() {
		return Stream.of(tipoServico.values()).map(tipoServico::name).toArray(String[]::new);
	}

	public static String[] montaArrayTipoPacotes() {
		return Stream.of(tipoPacote.values()).map(tipoPacote::name).toArray(String[]::new);
	}

	@Override
	public void validarData(String data) {
		boolean dataValida = GenericValidator.isDate(data, "dd/MM/yyyy", true);
		if (!dataValida) {
			throw new RegraNegocioException("Data inválida.");
		}
	}

	@Override
	public void validarValorNumerico(String valor) {
		boolean numeroValido = validDouble.isNumericValid(valor);
		if (!numeroValido) {
			throw new RegraNegocioException("Valor Inválido.");
		}
	}

	@Override
	public BigDecimal converter(String value) {
		return bigDecimalConverter.converter(value);
	}

	@Override
	public void validarStatus(String status) {
		StringBuilder todosStatus = new StringBuilder();

		for (StatusServico strStatus : StatusServico.values()) {
			todosStatus.append(strStatus).append(", ");
		}

		String statusString = todosStatus.length() > 0 
				? todosStatus.substring(0, todosStatus.length() - 2)
				: "";

		if (status == null) {
			throw new RegraNegocioException("Status inválido. Valores válidos: " + statusString);
		}

		boolean exists = Stream.of(StatusServico.values())
				.anyMatch(e -> e.name().equals(status));

		if (!exists) {
			throw new RegraNegocioException("Erro. Item " + status + " não existe.");
		}
	}

	@Override
	public void validarCep(String cep) {
		boolean cepValido = (cep.matches("\\d{8}"));
		if (!cepValido) {
			throw new RegraNegocioException("Cep inválido.");
		}
	}

	@Override
	public void validarTokenGoogle(String token) {
		if (!RESPONSE_PATTERN.matcher(token).matches()) {
			throw new RegraNegocioException("Token inválido.");
		}
	}

	@Override
	public void validarValorInteger(String valor) {
		this.validarValorInteger(valor, 10);
	}

	@Override
	public void validarValorInteger(String valor, int base) {
		if (valor.isEmpty()) {
			throw new RegraNegocioException(VALOR_INVALIDO);
		}
		for (int i = 0; i < valor.length(); i++) {
			if (i == 0 && valor.charAt(i) == '-') {
				if (valor.length() == 1) {
					throw new RegraNegocioException(VALOR_INVALIDO);
				}
				continue;
			}
			if (Character.digit(valor.charAt(i), base) < 0) {
				throw new RegraNegocioException(VALOR_INVALIDO);
			}
		}
	}

	@Override
	public void validarTipoServico(String tipo) {
		boolean exists = Stream.of(montaArrayTipoServicos())
				.anyMatch(item -> item.equals(tipo));

		if (!exists) {
			throw new RegraNegocioException("Erro. Tipo de Serviço inválido: Opções válidas: U, P.");
		}
	}

	@Override
	public void validarTipoPacote(String tipo) {
		boolean exists = Stream.of(montaArrayTipoPacotes())
				.anyMatch(item -> item.equals(tipo));

		if (!exists) {
			throw new RegraNegocioException(
					"Erro. Tipo de Pacote inválido: Opções válidas: I, A, E, C, F.");
		}
	}

}
