package io.github.hvogel.clientes.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.entity.Documento;
import io.github.hvogel.clientes.model.repository.DocumentoRepository;
import io.github.hvogel.clientes.service.DocumentoService;

@Service
public class DocumentoServiceImpl implements DocumentoService {
	
	private final DocumentoRepository repository;	
	
	public DocumentoServiceImpl(DocumentoRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Optional<Documento> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public Page<Documento> obterPorId(Integer id, Pageable pageable) {
		return repository.findByIdDocumento(id, pageable);
	}

	@Override
	public List<Documento> obterTodos() {
		return repository.findAll();
	}

	@Override
	public Page<Documento> pesquisarPelaDescricao(String descricao, Pageable pageable) {
		return repository.findByDescricaoContainsAllIgnoreCase(descricao, pageable);
	}

	@Override
	public Page<Documento> obterTodos(Pageable pageable) {
		return repository.findAll(pageable);
	}

}
