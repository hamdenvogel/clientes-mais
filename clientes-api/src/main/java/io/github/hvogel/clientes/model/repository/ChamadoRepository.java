package io.github.hvogel.clientes.model.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.infra.CommonRepository;
import io.github.hvogel.clientes.model.entity.Chamado;

public interface ChamadoRepository extends CommonRepository<Chamado> {

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from Chamado c where c.cliente.id = :clienteId")
	void deleteByClienteId(@Param("clienteId") Integer clienteId);

}
