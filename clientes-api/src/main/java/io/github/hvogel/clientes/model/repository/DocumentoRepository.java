package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
	Optional<Documento> findByDescricao(String descricao);
	long count();
	List<Documento> findAllByOrderByDescricaoAsc();	
	Page<Documento> findByDescricaoContainsAllIgnoreCase(String descricao, Pageable pageable);
	@Query("select d from Documento d where d.id = :id ")
	Page<Documento> findByIdDocumento(@Param("id") Integer id, Pageable pageable);

}
