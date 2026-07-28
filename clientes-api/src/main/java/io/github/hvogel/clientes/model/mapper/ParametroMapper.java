package io.github.hvogel.clientes.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import io.github.hvogel.clientes.model.entity.Parametro;
import io.github.hvogel.clientes.rest.dto.ParametroDTO;

@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParametroMapper {
	
	ParametroMapper INSTANCE = Mappers.getMapper(ParametroMapper.class);
	
	ParametroDTO toDto(Parametro parametro);

	Parametro toEntity(ParametroDTO parametroDTO);

}
