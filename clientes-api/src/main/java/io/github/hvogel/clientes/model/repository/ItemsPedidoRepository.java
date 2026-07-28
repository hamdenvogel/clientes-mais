package io.github.hvogel.clientes.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.ItemPedido;

public interface ItemsPedidoRepository extends JpaRepository<ItemPedido, Integer> {

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = """
		delete from meusservicos.item_pedido ip
		using meusservicos.pedido p, meusservicos.servicoprestado sp
		where ip.pedido_id = p.id
		  and p.servico_prestado_id = sp.id
		  and sp.id_cliente = :clienteId
		""", nativeQuery = true)
	void deleteByPedidoServicoPrestadoClienteId(@Param("clienteId") Integer clienteId);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = """
		delete from meusservicos.item_pedido ip
		using meusservicos.pedido p, meusservicos.servicoprestado sp
		where ip.pedido_id = p.id
		  and p.servico_prestado_id = sp.id
		  and sp.id_prestador = :prestadorId
		""", nativeQuery = true)
	void deleteByPedidoServicoPrestadoPrestadorId(@Param("prestadorId") Integer prestadorId);

}
