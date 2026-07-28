package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.Profissao;

public interface ProfissaoRepository extends JpaRepository<Profissao, Integer> {
	
	List<Profissao> findAllByOrderByDescricaoAsc();
	Optional<Profissao> findByDescricao(String descricao);	
	@Query("select p from Profissao p where upper(p.descricao) LIKE :descricao% order by p.descricao")
	List<Profissao> findByDescricaoStartsWith(@Param("descricao") String descricao);
}
