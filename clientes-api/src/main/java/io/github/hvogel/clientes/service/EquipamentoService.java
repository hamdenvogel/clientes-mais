package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.infra.AbstractService;
import io.github.hvogel.clientes.model.entity.Equipamento;
import io.github.hvogel.clientes.model.mapper.EquipamentoMapper;
import io.github.hvogel.clientes.model.repository.EquipamentoRepository;
import io.github.hvogel.clientes.rest.dto.EquipamentoDTO;

@Service
public class EquipamentoService extends AbstractService<Equipamento, EquipamentoDTO, EquipamentoRepository> {

	private final EquipamentoMapper equipamentoMapper;

	public EquipamentoService(EquipamentoRepository repository, EquipamentoMapper equipamentoMapper) {
		super(repository);
		this.equipamentoMapper = equipamentoMapper;
	}

	@Override
	public EquipamentoDTO convertToDto(Equipamento entity) {
		return equipamentoMapper.toDto(entity);
	}

	@Override
	public Equipamento convertToEntity(EquipamentoDTO dto) {
		return equipamentoMapper.toEntity(dto);
	}

	public List<Equipamento> obterEquipamentos() {
		return repository.findAllByOrderByDescricaoAsc();
	}

	public Optional<Equipamento> obterPorId(Long id) {
		return this.findOneById(id);
	}

	public Page<Equipamento> findByServicoPrestadoId(Integer id, Pageable pageable) {
		return repository.findByServicoPrestadoId(id, pageable);
	}

	public Page<Equipamento> findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(String descricao, Integer id,
			Pageable pageable) {
		return repository.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(descricao, id, pageable);
	}

	@Override
	public long count() {
		return repository.count();
	}
}
