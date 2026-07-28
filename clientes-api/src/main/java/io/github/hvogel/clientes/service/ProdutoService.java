package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.model.entity.Produto;

public interface ProdutoService {
	Produto salvar(Produto produto);
	Produto atualizar(Produto produto);
	void deletar(Produto produto);
	Optional<Produto> obterPorId(Integer id);
	Page<Produto> obterPorId(Integer id, Pageable pageable);
	List<Produto> obterTodos();
	void validar(Produto produto);
	void validarAtualizacao(Produto produto);
	Page<Produto> recuperarTodos(Pageable pageable);
	Page<Produto> pesquisarPelaDescricao(String descricao, Pageable pageable);
	Page<Produto> executaCriteria(List<SearchCriteria> searchCriteria, Pageable pageable);	
}
