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
import io.github.hvogel.clientes.model.entity.Equipamento;

@Repository
public interface EquipamentoRepository extends CommonRepository<Equipamento> {
	
	Optional<Equipamento> findByDescricao(String descricao);
	
	boolean existsByDescricao(String descricao);
	
	long count();
	
	List<Equipamento> findAllByOrderByDescricaoAsc();
	
	@Query("select e from Equipamento e where e.id = :id")
	Page<Equipamento> findByIdSolucao(@Param("id") Integer id, Pageable pageable);
	
	@Query("select e from Equipamento e where e.id = :id")
	Equipamento obterPorId(@Param("id") Integer id);
	
	Page<Equipamento> findByServicoPrestadoId(Integer id, Pageable pageable);
	
	Page<Equipamento> findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(String descricao, Integer id, Pageable pageable);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from Equipamento e where e.servicoPrestado.cliente.id = :clienteId")
	void deleteByServicoPrestadoClienteId(@Param("clienteId") Integer clienteId);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from Equipamento e where e.servicoPrestado.prestador.id = :prestadorId")
	void deleteByServicoPrestadoPrestadorId(@Param("prestadorId") Integer prestadorId);

}
