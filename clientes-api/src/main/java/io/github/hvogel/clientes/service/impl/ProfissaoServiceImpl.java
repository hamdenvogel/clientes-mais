package io.github.hvogel.clientes.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.entity.Profissao;
import io.github.hvogel.clientes.model.repository.ProfissaoRepository;
import io.github.hvogel.clientes.service.ProfissaoService;


@Service
public class ProfissaoServiceImpl implements ProfissaoService {
	
	private final ProfissaoRepository repository;
	
	public ProfissaoServiceImpl(ProfissaoRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Optional<Profissao> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Profissao> obterTodos() {
		return repository.findAllByOrderByDescricaoAsc();
	}

	@Override
	public List<Profissao> obterPorDescricao(String descricao) {
		return repository.findByDescricaoStartsWith(descricao);
	}

}
