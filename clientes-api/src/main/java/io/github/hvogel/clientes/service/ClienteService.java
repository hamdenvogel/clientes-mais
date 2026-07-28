package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.model.entity.Cliente;

public interface ClienteService {
	Cliente salvar(Cliente cliente);
	Cliente atualizar(Cliente cliente);
	void deletar(Cliente cliente);
	Optional<Cliente> obterPorId(Integer id);
	List<Cliente> obterTodos();
	void validar(Cliente cliente);
	void validarAtualizacao(Cliente cliente);
	Page<Cliente> recuperarTodos(Pageable pageable);
	Page<Cliente> pesquisarPeloNome(String nome, Pageable pageable);
}
