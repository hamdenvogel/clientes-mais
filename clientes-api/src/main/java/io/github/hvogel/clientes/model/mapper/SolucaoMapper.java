package io.github.hvogel.clientes.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import io.github.hvogel.clientes.model.entity.Solucao;
import io.github.hvogel.clientes.rest.dto.SolucaoDTO;

@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SolucaoMapper {
	
	SolucaoMapper INSTANCE = Mappers.getMapper(SolucaoMapper.class);
	
	@Mapping(source = "solucao.servicoPrestado.id", target = "servicoPrestadoId")
	SolucaoDTO toDto(Solucao solucao);

	@Mapping(source = "solucaoDTO.servicoPrestadoId", target = "servicoPrestado.id")
	Solucao toEntity(SolucaoDTO solucaoDTO);

}
