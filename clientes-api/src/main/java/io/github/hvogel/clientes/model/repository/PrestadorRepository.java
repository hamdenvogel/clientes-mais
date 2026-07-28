package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.Prestador;

public interface PrestadorRepository extends JpaRepository<Prestador, Integer>, JpaSpecificationExecutor<Prestador> {
	Optional<Prestador> findByNome(String nome);
	boolean existsByNome(String nome);
	long count();
	Optional<Prestador> findByCpf(String cpf);
	Optional<Prestador> findByCpfAndIdNot(String cpf, Integer id);
	Optional<Prestador> findByNomeAndIdNot(String nome, Integer id);
	List<Prestador> findAllByOrderByNomeAsc();
	@Query("select p from Prestador p where p.id = :id ")
	Page<Prestador> findByIdPrestador(@Param("id") Integer id, Pageable pageable);
	Page<Prestador> findByNomeContainsAllIgnoreCase(String nome, Pageable pageable);
}
