package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.hvogel.clientes.infra.CommonRepository;
import io.github.hvogel.clientes.model.entity.Atividade;

@Repository
public interface AtividadeRepository extends CommonRepository<Atividade> {
	
	Optional<Atividade> findByDescricao(String descricao);
	
	boolean existsByDescricao(String descricao);
	
	long count();
	List<Atividade> findAllByOrderByDescricaoAsc();
	
	@Query("select a from Atividade a where a.id = :id")
	Page<Atividade> findByIdAtividade(@Param("id") Integer id, Pageable pageable);	
	
	@Query("select a from Atividade a where a.id = :id")
	Atividade obterPorId(@Param("id") Integer id);
	
}
