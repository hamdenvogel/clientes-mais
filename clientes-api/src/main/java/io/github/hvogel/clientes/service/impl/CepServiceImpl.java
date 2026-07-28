package io.github.hvogel.clientes.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Endereco;
import io.github.hvogel.clientes.rest.dto.EnderecoDTO;
import io.github.hvogel.clientes.service.CepService;
import io.github.hvogel.clientes.service.ValidadorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CepServiceImpl implements CepService {

	private final RestTemplate restTemplate;

	@Value("${via-cep.format}")
	private String viaCepFormat;

	private final ValidadorService validadorService;

	@Override
	public EnderecoDTO pesquisarCep(String cep) {
		try {
			validadorService.validarCep(cep);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		Map<String, String> params = new HashMap<>();
		params.put("cep", cep);

		Endereco endereco = restTemplate.getForObject(viaCepFormat, Endereco.class,
				params);

		if (endereco == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado para o CEP informado.");
		}

		EnderecoDTO enderecoDTO = new EnderecoDTO();

		String bairro = Optional.ofNullable(endereco.getBairro()).orElse("");
		enderecoDTO.setBairro(bairro);

	String cepValue = Optional.ofNullable(endereco.getCep()).orElse("");
	enderecoDTO.setCep(cepValue);

		String localidade = Optional.ofNullable(endereco.getLocalidade()).orElse("");
		enderecoDTO.setLocalidade(localidade);

		String logradouro = Optional.ofNullable(endereco.getLogradouro()).orElse("");
		enderecoDTO.setLogradouro(logradouro);

		String uf = Optional.ofNullable(endereco.getUf()).orElse("");
		enderecoDTO.setUf(uf);

		return enderecoDTO;
	}

}
