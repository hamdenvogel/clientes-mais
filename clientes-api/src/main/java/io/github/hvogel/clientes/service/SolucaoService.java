package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.infra.AbstractService;
import io.github.hvogel.clientes.model.entity.Solucao;
import io.github.hvogel.clientes.model.mapper.SolucaoMapper;
import io.github.hvogel.clientes.model.repository.SolucaoRepository;
import io.github.hvogel.clientes.rest.dto.SolucaoDTO;

@Service
public class SolucaoService extends AbstractService<Solucao, SolucaoDTO, SolucaoRepository> {

	private final SolucaoMapper solucaoMapper;

	@Autowired
	public SolucaoService(SolucaoRepository repository, SolucaoMapper solucaoMapper) {
		super(repository);
		this.solucaoMapper = solucaoMapper;
	}

	@Override
	public SolucaoDTO convertToDto(Solucao entity) {
		return solucaoMapper.toDto(entity);
	}

	@Override
	public Solucao convertToEntity(SolucaoDTO dto) {
		return solucaoMapper.toEntity(dto);
	}

	public List<Solucao> obterSolucoes() {
		return repository.findAllByOrderByDescricaoAsc();
	}

	public Optional<Solucao> obterPorId(Long id) {
		return this.findOneById(id);
	}

	public Page<Solucao> findByServicoPrestadoId(Integer id, Pageable pageable) {
		return repository.findByServicoPrestadoId(id, pageable);
	}

	public Page<Solucao> findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(String descricao, Integer id,
			Pageable pageable) {
		return repository.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(descricao, id, pageable);
	}

	@Override
	public long count() {
		return repository.count();
	}

}
