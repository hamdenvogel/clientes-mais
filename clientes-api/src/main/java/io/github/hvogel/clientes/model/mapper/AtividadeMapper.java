package io.github.hvogel.clientes.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import io.github.hvogel.clientes.model.entity.Atividade;
import io.github.hvogel.clientes.rest.dto.AtividadeDTO;

@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AtividadeMapper {
	
	AtividadeMapper INSTANCE = Mappers.getMapper(AtividadeMapper.class);
	
	AtividadeDTO toDto(Atividade atividade);

	Atividade toEntity(AtividadeDTO atividadeDTO);

}
