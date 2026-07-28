package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.model.entity.Documento;

public interface DocumentoService {
	Optional<Documento> obterPorId(Integer id);
	Page<Documento> obterPorId(Integer id, Pageable pageable);
	List<Documento> obterTodos();
	Page<Documento> pesquisarPelaDescricao(String descricao, Pageable pageable);
	Page<Documento> obterTodos(Pageable pageable);

}
