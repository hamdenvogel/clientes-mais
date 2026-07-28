package io.github.hvogel.clientes.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.hvogel.clientes.infra.AbstractService;
import io.github.hvogel.clientes.model.entity.Chamado;
import io.github.hvogel.clientes.model.mapper.ChamadoMapper;
import io.github.hvogel.clientes.model.repository.ChamadoRepository;
import io.github.hvogel.clientes.rest.dto.ChamadoDTO;

@Service
public class ChamadoService extends AbstractService<Chamado, ChamadoDTO, ChamadoRepository> {

	private final ChamadoMapper chamadoMapper;
	private final ChamadoRepository chamadoRepository;

	public ChamadoService(ChamadoRepository repository, ChamadoMapper chamadoMapper,
			ChamadoRepository chamadoRepository) {
		super(repository);
		this.chamadoMapper = chamadoMapper;
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public ChamadoDTO convertToDto(Chamado entity) {
		return chamadoMapper.toDto(entity);
	}

	@Override
	public Chamado convertToEntity(ChamadoDTO dto) {
		return chamadoMapper.toEntity(dto);
	}

	public Optional<Chamado> obterPorId(Long id) {
		return this.findOneById(id);
	}

	@Transactional
	public Chamado salvar(Chamado chamado) {
		return this.chamadoRepository.save(chamado);
	}

}
