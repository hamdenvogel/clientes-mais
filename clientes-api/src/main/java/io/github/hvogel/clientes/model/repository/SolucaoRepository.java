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
import io.github.hvogel.clientes.model.entity.Solucao;

@Repository
public interface SolucaoRepository extends CommonRepository<Solucao> {
	
	Optional<Solucao> findByDescricao(String descricao);
	
	boolean existsByDescricao(String descricao);
	
	long count();
	
	List<Solucao> findAllByOrderByDescricaoAsc();
	
	@Query("select s from Solucao s where s.id = :id")
	Page<Solucao> findByIdSolucao(@Param("id") Integer id, Pageable pageable);
	
	@Query("select s from Solucao s where s.id = :id")
	Solucao obterPorId(@Param("id") Integer id);
	
	Page<Solucao> findByServicoPrestadoId(Integer id, Pageable pageable);
	
	Page<Solucao> findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(String descricao, Integer id, Pageable pageable);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from Solucao s where s.servicoPrestado.cliente.id = :clienteId")
	void deleteByServicoPrestadoClienteId(@Param("clienteId") Integer clienteId);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from Solucao s where s.servicoPrestado.prestador.id = :prestadorId")
	void deleteByServicoPrestadoPrestadorId(@Param("prestadorId") Integer prestadorId);


}
