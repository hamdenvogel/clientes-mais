package io.github.hvogel.clientes.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.model.entity.Pacote;

public interface PacoteService {
	Pacote salvar(Pacote pacote);
	Pacote atualizar(Pacote pacote);
	void deletar(Pacote pacote);
	Optional<Pacote> obterPorId(Integer id);
	Page<Pacote> obterPorId(Integer id, Pageable pageable);
	Page<Pacote> pesquisarPelaDescricao(String descricao, Pageable pageable);
	List<Pacote> obterTodos();
	void validar(Pacote pacote);
	void validarAtualizacao(Pacote pacote);
	Page<Pacote> recuperarTodos(Pageable pageable);
	List<BigDecimal> iPercentual();
	List<BigDecimal> aPercentual();
	List<BigDecimal> ePercentual();
	List<BigDecimal> cPercentual();
	List<BigDecimal> fPercentual();
	
}
