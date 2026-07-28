package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.model.entity.Prestador;

public interface PrestadorService {
	Prestador salvar(Prestador prestador);
	Prestador atualizar(Prestador prestador);
	void deletar(Prestador prestador);
	Optional<Prestador> obterPorId(Integer id);
	Page<Prestador> obterPorId(Integer id, Pageable pageable);
	List<Prestador> obterTodos();
	void validar(Prestador prestador);
	void validarAtualizacao(Prestador prestador);
	Page<Prestador> recuperarTodos(Pageable pageable);
	Page<Prestador> pesquisarPeloNome(String nome, Pageable pageable);
	Page<Prestador> executaCriteria(List<SearchCriteria> searchCriteria, Pageable pageable);	
}
