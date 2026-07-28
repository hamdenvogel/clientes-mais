package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.hvogel.clientes.infra.CommonRepository;
import io.github.hvogel.clientes.model.entity.Diagnostico;

@Repository
public interface DiagnosticoRepository extends CommonRepository<Diagnostico>{
	
	Optional<Diagnostico> findByDescricao(String descricao);
	
	boolean existsByDescricao(String descricao);
	
	long count();
	
	List<Diagnostico> findAllByOrderByDescricaoAsc();
	
	@Query("select d from Diagnostico d where d.id = :id")
	Page<Diagnostico> findByIdDiagnostico(@Param("id") Integer id, Pageable pageable);	
	
	@Query("select d from Diagnostico d where d.id = :id")
	Diagnostico obterPorId(@Param("id") Integer id);	
	
	Page<Diagnostico> findByServicoPrestadoId(Integer id, Pageable pageable);	
	
	Page<Diagnostico> findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(String descricao, Integer id, Pageable pageable);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from Diagnostico d where d.servicoPrestado.cliente.id = :clienteId")
	void deleteByServicoPrestadoClienteId(@Param("clienteId") Integer clienteId);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from Diagnostico d where d.servicoPrestado.prestador.id = :prestadorId")
	void deleteByServicoPrestadoPrestadorId(@Param("prestadorId") Integer prestadorId);
}
