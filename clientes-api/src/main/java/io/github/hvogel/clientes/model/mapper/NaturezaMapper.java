package io.github.hvogel.clientes.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import io.github.hvogel.clientes.model.entity.Natureza;
import io.github.hvogel.clientes.rest.dto.NaturezaDTO;

@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NaturezaMapper {
	
	NaturezaMapper INSTANCE = Mappers.getMapper(NaturezaMapper.class);
	
	NaturezaDTO toDto(Natureza natureza);

	Natureza toEntity(NaturezaDTO naturezaDTO);

}
