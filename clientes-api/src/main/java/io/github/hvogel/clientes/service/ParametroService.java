package io.github.hvogel.clientes.service;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.infra.AbstractService;
import io.github.hvogel.clientes.model.entity.Parametro;
import io.github.hvogel.clientes.model.mapper.ParametroMapper;
import io.github.hvogel.clientes.model.repository.ParametroRepository;
import io.github.hvogel.clientes.rest.dto.ParametroDTO;

@Service
public class ParametroService extends AbstractService<Parametro, ParametroDTO, ParametroRepository> {

	private final ParametroMapper parametroMapper;

	public ParametroService(ParametroRepository repository, ParametroMapper parametroMapper) {
		super(repository);
		this.parametroMapper = parametroMapper;
	}

	@Override
	public ParametroDTO convertToDto(Parametro entity) {
		return parametroMapper.toDto(entity);
	}

	@Override
	public Parametro convertToEntity(ParametroDTO dto) {
		return parametroMapper.toEntity(dto);
	}

}
