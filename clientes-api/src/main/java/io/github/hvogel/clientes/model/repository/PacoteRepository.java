package io.github.hvogel.clientes.model.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.Pacote;

public interface PacoteRepository extends JpaRepository<Pacote, Integer> {
	
	long count();
	Optional<Pacote> findByDescricao(String descricao);
	List<Pacote> findAllByOrderByDescricaoAsc();
	boolean existsByDescricao(String descricao);
	@Query("select p from Pacote p where p.id = :id ")
	Page<Pacote> findByIdPacote(@Param("id") Integer id, Pageable pageable);
	Optional<Pacote> findByDescricaoAndIdNot(String descricao, Integer id);
	Page<Pacote> findByDescricaoContainsAllIgnoreCase(String descricao, Pageable pageable);
	
	@Query(value="select " + 
			"round( " + 
			"cast( " + 
			"count(case pacote.status " + 
			"when 'I' then 1 " + 
			"else null " + 
			"end) as decimal) / " + 
			"cast ( " + 
			"count(*) as decimal)*100, 2) " + 
			"as iPercentual " + 
			"from meusservicos.pacote pacote " + 
			"where pacote.status in ('I','A','E','C','F') " + 
			"and pacote.data is not null", nativeQuery = true)
	List<BigDecimal> iPercentual();

	@Query(value="select " + 
			"round( " + 
			"cast( " + 
			"count(case pacote.status " + 
			"when 'A' then 1 " + 
			"else null " + 
			"end) as decimal) / " + 
			"cast ( " + 
			"count(*) as decimal)*100, 2) " + 
			"as iPercentual " + 
			"from meusservicos.pacote pacote " + 
			"where pacote.status in ('I','A','E','C','F') " + 
			"and pacote.data is not null", nativeQuery = true)
	List<BigDecimal> aPercentual();
	
	@Query(value="select " + 
			"round( " + 
			"cast( " + 
			"count(case pacote.status " + 
			"when 'E' then 1 " + 
			"else null " + 
			"end) as decimal) / " + 
			"cast ( " + 
			"count(*) as decimal)*100, 2) " + 
			"as iPercentual " + 
			"from meusservicos.pacote pacote " + 
			"where pacote.status in ('I','A','E','C','F') " + 
			"and pacote.data is not null", nativeQuery = true)
	List<BigDecimal> ePercentual();
	
	@Query(value="select " + 
			"round( " + 
			"cast( " + 
			"count(case pacote.status " + 
			"when 'C' then 1 " + 
			"else null " + 
			"end) as decimal) / " + 
			"cast ( " + 
			"count(*) as decimal)*100, 2) " + 
			"as iPercentual " + 
			"from meusservicos.pacote pacote " + 
			"where pacote.status in ('I','A','E','C','F') " + 
			"and pacote.data is not null", nativeQuery = true)
	List<BigDecimal> cPercentual();
	
	@Query(value="select " + 
			"round( " + 
			"cast( " + 
			"count(case pacote.status " + 
			"when 'F' then 1 " + 
			"else null " + 
			"end) as decimal) / " + 
			"cast ( " + 
			"count(*) as decimal)*100, 2) " + 
			"as iPercentual " + 
			"from meusservicos.pacote pacote " + 
			"where pacote.status in ('I','A','E','C','F') " + 
			"and pacote.data is not null", nativeQuery = true)
	List<BigDecimal> fPercentual();
}
