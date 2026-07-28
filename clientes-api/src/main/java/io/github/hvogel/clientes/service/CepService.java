package io.github.hvogel.clientes.service;

import io.github.hvogel.clientes.rest.dto.EnderecoDTO;

public interface CepService {
	EnderecoDTO pesquisarCep(String cep);
}
