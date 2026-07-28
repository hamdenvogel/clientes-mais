package io.github.hvogel.clientes.model.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.Pedido;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;

public interface PedidosRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByServicoPrestado(ServicoPrestado servicoPrestado);

    @Query(" select p from Pedido p left join fetch p.itens where p.id = :id ")
	Optional<Pedido> findByIdFetchItens(@Param("id") Integer id);       
    
    @Query(" select p from Pedido p left join fetch p.itens where p.dataPedido = :dataPedido ")
	List<Pedido> findByIdFetchItensByDataPedido(@Param("dataPedido") LocalDate dataPedido);   
    
    @Query(" select p from Pedido p left join fetch p.itens order by p.dataPedido ")
	List<Pedido> findByIdFetchItensAll();   
    
    @Query(" select distinct p from Pedido p inner join fetch p.itens where p.id = :id ")
	List<Pedido> findByIdFetchItensId(@Param("id") Integer id);     
    
    List<Pedido> findByItensPedidoId(Integer id);    

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Pedido p where p.servicoPrestado.cliente.id = :clienteId")
    void deleteByServicoPrestadoClienteId(@Param("clienteId") Integer clienteId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Pedido p where p.servicoPrestado.prestador.id = :prestadorId")
    void deleteByServicoPrestadoPrestadorId(@Param("prestadorId") Integer prestadorId);
    
    List<Pedido> findAllByOrderByItensProdutoDescricaoAsc();
    
}

