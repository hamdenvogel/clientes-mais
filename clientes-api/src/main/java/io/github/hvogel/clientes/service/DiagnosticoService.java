package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.infra.AbstractService;
import io.github.hvogel.clientes.model.entity.Diagnostico;
import io.github.hvogel.clientes.model.mapper.DiagnosticoMapper;
import io.github.hvogel.clientes.model.repository.DiagnosticoRepository;
import io.github.hvogel.clientes.rest.dto.DiagnosticoDTO;

@Service
public class DiagnosticoService extends AbstractService<Diagnostico, DiagnosticoDTO, DiagnosticoRepository> {

	private final DiagnosticoMapper diagnosticoMapper;

	public DiagnosticoService(DiagnosticoRepository repository, DiagnosticoMapper diagnosticoMapper) {
		super(repository);
		this.diagnosticoMapper = diagnosticoMapper;
	}

	@Override
	public DiagnosticoDTO convertToDto(Diagnostico entity) {
		return diagnosticoMapper.toDto(entity);
	}

	@Override
	public Diagnostico convertToEntity(DiagnosticoDTO dto) {
		return diagnosticoMapper.toEntity(dto);
	}

	public List<Diagnostico> obterDiagnosticos() {
		return repository.findAllByOrderByDescricaoAsc();
	}

	public Optional<Diagnostico> obterPorId(Long id) {
		return this.findOneById(id);
	}

	public Page<Diagnostico> findByServicoPrestadoId(Integer id, Pageable pageable) {
		return repository.findByServicoPrestadoId(id, pageable);
	}

	public Page<Diagnostico> findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(String descricao, Integer id,
			Pageable pageable) {
		return repository.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(descricao, id, pageable);
	}

	@Override
	public long count() {
		return repository.count();
	}

}
