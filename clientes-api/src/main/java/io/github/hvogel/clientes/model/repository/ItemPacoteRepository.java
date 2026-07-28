package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.ItemPacote;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;

public interface ItemPacoteRepository extends JpaRepository<ItemPacote, Integer> {
	
	long count();
	
	@Query("select i from ItemPacote i where i.id = :id ")
	Page<ItemPacote> findByIdItemPacote(@Param("id") Integer id, Pageable pageable);	
	
	@Query("select i from ItemPacote i join i.pacote p " +
			" where upper(p.descricao) like upper (:descricao) ")
	Page<ItemPacote> findByDescricaoPacote(@Param("descricao") String descricao, Pageable pageable);
	
	@Query("select i from ItemPacote i join i.pacote p " +
			" where p.id = :id ")
	Page<ItemPacote> findByIdPacote(@Param("id") Integer id, Pageable pageable);
	
	List<ItemPacote> findAllByOrderByPacoteAsc();
	
	@Query("select i from ItemPacote i join i.pacote p join i.servicoPrestado s " +
	"where p.id =:packageId and s.id =:serviceId ")
	Optional<ItemPacote> existsByPacoteAndServicoPrestado(@Param("packageId") Integer packageId, 
			@Param("serviceId") Integer serviceId); 
	
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from ItemPacote i where i.pacote.id =:packageId and i.servicoPrestado.id =:serviceId")
	void deletarPorPacoteEServicoPrestado(@Param("packageId") Integer packageId, @Param("serviceId") Integer serviceId); 
	
	@Query("select i from ItemPacote i join i.pacote p join i.servicoPrestado s " +
	"where s.descricao LIKE %:descricao%")
	Optional<ItemPacote> searchByServicoPrestadoDescricaoLike(@Param("descricao") String descricao);
	
	long countByPacoteId(Integer idPacote);
	
	boolean existsByServicoPrestado(ServicoPrestado servicoPrestado);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from ItemPacote i where i.servicoPrestado.cliente.id = :clienteId")
	void deleteByServicoPrestadoClienteId(@Param("clienteId") Integer clienteId);

	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("delete from ItemPacote i where i.servicoPrestado.prestador.id = :prestadorId")
	void deleteByServicoPrestadoPrestadorId(@Param("prestadorId") Integer prestadorId);
}
