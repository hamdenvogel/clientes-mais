package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import io.github.hvogel.clientes.model.entity.Profissao;

public interface ProfissaoService {
	
	Optional<Profissao> obterPorId(Integer id);
	List<Profissao> obterTodos();
	List<Profissao> obterPorDescricao (String descricao);
	
}
