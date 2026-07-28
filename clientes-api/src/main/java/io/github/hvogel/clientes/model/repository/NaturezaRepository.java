package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.infra.CommonRepository;
import io.github.hvogel.clientes.model.entity.Natureza;

public interface NaturezaRepository extends CommonRepository<Natureza> {
	Optional<Natureza> findByDescricao(String descricao);
	boolean existsByDescricao(String descricao);
	long count();
	List<Natureza> findAllByOrderByDescricaoAsc();
	@Query("select n from Natureza n where n.id = :id ")
	Page<Natureza> findByIdNatureza(@Param("id") Integer id, Pageable pageable);
	
}
