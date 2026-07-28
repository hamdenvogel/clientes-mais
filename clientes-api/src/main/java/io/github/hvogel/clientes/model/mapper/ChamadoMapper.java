package io.github.hvogel.clientes.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import io.github.hvogel.clientes.model.entity.Chamado;
import io.github.hvogel.clientes.rest.dto.ChamadoDTO;

@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChamadoMapper {
	
	ChamadoMapper INSTANCE = Mappers.getMapper(ChamadoMapper.class);
	
	@Mapping(source = "cliente.id", target = "clienteId")
	ChamadoDTO toDto(Chamado chamado);

	@Mapping(source = "clienteId", target = "cliente.id")
	Chamado toEntity(ChamadoDTO chamadoDTO);
}
