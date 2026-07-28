package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.Produto;

public interface ProdutosRepository extends JpaRepository<Produto,Integer>, JpaSpecificationExecutor<Produto> {	
	Optional<Produto> findByDescricao(String descricao);
	boolean existsByDescricao(String descricao);
	long count();
	@Query("select p from Produto p where p.id = :id ")
	Page<Produto> findByIdProduto(@Param("id") Integer id, Pageable pageable);
	Page<Produto> findByDescricaoContainsAllIgnoreCase(String descricao, Pageable pageable);
	List<Produto> findAllByOrderByDescricaoAsc();
	Optional<Produto> findByDescricaoAndIdNot(String descricao, Integer id);

}
