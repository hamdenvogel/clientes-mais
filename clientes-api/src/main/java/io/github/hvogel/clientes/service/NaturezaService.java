package io.github.hvogel.clientes.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.infra.AbstractService;
import io.github.hvogel.clientes.model.entity.Natureza;
import io.github.hvogel.clientes.model.mapper.NaturezaMapper;
import io.github.hvogel.clientes.model.repository.NaturezaRepository;
import io.github.hvogel.clientes.rest.dto.NaturezaDTO;

@Service
public class NaturezaService extends AbstractService<Natureza, NaturezaDTO, NaturezaRepository> {

	private final NaturezaMapper naturezaMapper;

	public NaturezaService(NaturezaRepository repository, NaturezaMapper naturezaMapper) {
		super(repository);
		this.naturezaMapper = naturezaMapper;
	}

	@Override
	public NaturezaDTO convertToDto(Natureza entity) {
		return naturezaMapper.toDto(entity);
	}

	@Override
	public Natureza convertToEntity(NaturezaDTO dto) {
		return naturezaMapper.toEntity(dto);
	}

	public Optional<Natureza> obterPorId(Long id) {
		return this.findOneById(id);
	}

}
