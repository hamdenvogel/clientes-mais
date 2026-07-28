package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.Imagem;

public interface ImagemRepository extends JpaRepository<Imagem, Integer> {

	Imagem findByFileName(String fileName);

	Imagem findByUuid(String uuid);

	List<Imagem> findAllByOrderByFileNameAsc();

	List<Imagem> findByChaveIdAndDocumentoId(Integer chaveId, Integer documentoId);

	@Query("select i from Imagem i where i.chaveId =:chaveId and i.documento.id =:documentoId")
	Optional<Imagem> getByChaveIdAndDocumentoId(Integer chaveId, Integer documentoId);

	@Modifying
	@Query("delete from Imagem i where i.chaveId =:chaveId and i.documento.id =:documentoId")
	void deleteByChaveIdAndDocumentoId(@Param("chaveId") Integer chaveId, @Param("documentoId") Integer documentoId);

}
