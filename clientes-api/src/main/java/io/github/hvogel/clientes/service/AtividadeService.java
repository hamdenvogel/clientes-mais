package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.infra.AbstractService;
import io.github.hvogel.clientes.model.entity.Atividade;
import io.github.hvogel.clientes.model.mapper.AtividadeMapper;
import io.github.hvogel.clientes.model.repository.AtividadeRepository;
import io.github.hvogel.clientes.rest.dto.AtividadeDTO;

@Service
public class AtividadeService extends AbstractService<Atividade, AtividadeDTO, AtividadeRepository> {

	private final AtividadeMapper atividadeMapper;

	public AtividadeService(AtividadeRepository repository, AtividadeMapper atividadeMapper) {
		super(repository);
		this.atividadeMapper = atividadeMapper;
	}

	@Override
	public AtividadeDTO convertToDto(Atividade entity) {
		return atividadeMapper.toDto(entity);
	}

	@Override
	public Atividade convertToEntity(AtividadeDTO dto) {
		return atividadeMapper.toEntity(dto);
	}

	public List<Atividade> obterAtividades() {
		return repository.findAllByOrderByDescricaoAsc();
	}

	public Optional<Atividade> obterPorId(Long id) {
		return this.findOneById(id);
	}

}
