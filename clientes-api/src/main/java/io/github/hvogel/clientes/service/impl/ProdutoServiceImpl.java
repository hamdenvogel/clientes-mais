package io.github.hvogel.clientes.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.model.repository.ProdutosRepository;
import io.github.hvogel.clientes.model.specification.ProdutoSpecification;
import io.github.hvogel.clientes.service.ProdutoService;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	private final ProdutosRepository repository;

	public ProdutoServiceImpl(ProdutosRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	@Transactional
	public Produto salvar(Produto produto) {
		validar(produto);
		return repository.save(produto);
	}

	@Override
	@Transactional
	public Produto atualizar(Produto produto) {
		Objects.requireNonNull(produto.getId());
		validarAtualizacao(produto);
		return repository.save(produto);
	}

	@Override
	public void deletar(Produto produto) {
		repository.delete(produto);
	}

	@Override
	public Optional<Produto> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public Page<Produto> obterPorId(Integer id, Pageable pageable) {
		return repository.findByIdProduto(id, pageable);
	}

	@Override
	public List<Produto> obterTodos() {
		return repository.findAllByOrderByDescricaoAsc();
	}

	@Override
	public void validar(Produto produto) {
		String nomeComAInicialMaiuscula = produto.getDescricao();
		nomeComAInicialMaiuscula = nomeComAInicialMaiuscula.substring(0, 1).toUpperCase() +
				nomeComAInicialMaiuscula.substring(1).toLowerCase();

		produto.setDescricao(nomeComAInicialMaiuscula);
		Optional<Produto> descricaoExistente = repository.findByDescricao(produto.getDescricao().trim());
		if (descricaoExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Descrição do produto já cadastrada.");
		}
	}

	@Override
	public void validarAtualizacao(Produto produto) {
		String nomeComAInicialMaiuscula = produto.getDescricao();
		nomeComAInicialMaiuscula = nomeComAInicialMaiuscula.substring(0, 1).toUpperCase() +
				nomeComAInicialMaiuscula.substring(1).toLowerCase();

		produto.setDescricao(nomeComAInicialMaiuscula);

		Optional<Produto> descricaoExistente = repository.findByDescricaoAndIdNot(produto.getDescricao(),
				produto.getId());
		if (descricaoExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Descrição já cadastrada para outro produto.");
		}
	}

	@Override
	public Page<Produto> recuperarTodos(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<Produto> pesquisarPelaDescricao(String descricao, Pageable pageable) {
		return repository.findByDescricaoContainsAllIgnoreCase(descricao, pageable);
	}

	@Override
	public Page<Produto> executaCriteria(List<SearchCriteria> searchCriteria, Pageable pageable) {
		ProdutoSpecification produtoSpecification = new ProdutoSpecification();
		searchCriteria.stream().map(searchCriteriaValue -> new SearchCriteria(searchCriteriaValue.getKey(),
				searchCriteriaValue.getValue(), searchCriteriaValue.getOperation())).forEach(produtoSpecification::add);
		return repository.findAll(produtoSpecification, pageable);
	}

}
