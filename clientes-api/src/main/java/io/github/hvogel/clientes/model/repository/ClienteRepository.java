package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.hvogel.clientes.model.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>, JpaSpecificationExecutor<Cliente> {
	
	Optional<Cliente> findByCpf(String cpf);
	Optional<Cliente> findByCpfAndIdNot(String cpf, Integer id);
	Optional<Cliente> findByNome(String nome);
	Optional<Cliente> findByNomeAndIdNot(String nome, Integer id);
	List<Cliente> findAllByOrderByNomeAsc();
	Page<Cliente> findByNomeContainsAllIgnoreCase(String descricao, Pageable pageable);	
}
