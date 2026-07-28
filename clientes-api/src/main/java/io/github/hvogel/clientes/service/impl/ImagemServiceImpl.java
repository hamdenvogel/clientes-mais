package io.github.hvogel.clientes.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.hvogel.clientes.model.entity.Imagem;
import io.github.hvogel.clientes.model.repository.ImagemRepository;
import io.github.hvogel.clientes.service.ImagemService;


@Service
public class ImagemServiceImpl implements ImagemService {
	
	private final ImagemRepository repository;
			
	public ImagemServiceImpl(ImagemRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Imagem save(Imagem imagem) throws NullPointerException {
		if (imagem == null)
			throw new NullPointerException("Image Data NULL");
		return repository.save(imagem);
	}

	@Override
	public Imagem findByFileName(String fileName) {
		return this.repository.findByFileName(fileName);
	}

	@Override
	public Imagem findByUuid(String uuid) {
		return this.repository.findByUuid(uuid);
	}

	@Override
	public List<Imagem> findAll() {
		return repository.findAllByOrderByFileNameAsc();
	}

	@Override
	public Optional<Imagem> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Imagem> findByChaveIdAndDocumentoId(Integer chaveId, Integer documentoId) {
		return repository.findByChaveIdAndDocumentoId(chaveId, documentoId);
	}

	@Override
	@Transactional
	public void deletar(Imagem imagem) {
		repository.delete(imagem);		
	}

	@Override
	@Transactional
	public void deleteByChaveIdAndDocumentoId(Integer chaveId, Integer documentoId) {
		repository.deleteByChaveIdAndDocumentoId(chaveId, documentoId);		
	}

	@Override	
	public Optional<Imagem> getByChaveIdAndDocumentoId(Integer chaveId, Integer documentoId) {
		return repository.getByChaveIdAndDocumentoId(chaveId, documentoId);
	}

}
