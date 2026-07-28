package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import io.github.hvogel.clientes.model.entity.Imagem;

public interface ImagemService {	
	Imagem save(Imagem image);
	Imagem findByFileName(String fileName);
	Imagem findByUuid(String uuid);	
	List<Imagem> findAll();
	Optional<Imagem> obterPorId(Integer id);
	List<Imagem> findByChaveIdAndDocumentoId(Integer chaveId, Integer documentoId);
	Optional<Imagem> getByChaveIdAndDocumentoId(Integer chaveId, Integer documentoId);
	void deletar(Imagem imagem);
	void deleteByChaveIdAndDocumentoId(Integer chaveId, Integer documentoId);
}
