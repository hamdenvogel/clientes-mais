package io.github.hvogel.clientes.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import io.github.hvogel.clientes.model.entity.Diagnostico;
import io.github.hvogel.clientes.rest.dto.DiagnosticoDTO;

@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiagnosticoMapper {

	DiagnosticoMapper INSTANCE = Mappers.getMapper(DiagnosticoMapper.class);
	
	@Mapping(source = "diagnostico.servicoPrestado.id", target = "servicoPrestadoId")
	DiagnosticoDTO toDto(Diagnostico diagnostico);

	@Mapping(source = "diagnosticoDTO.servicoPrestadoId", target = "servicoPrestado.id")
	Diagnostico toEntity(DiagnosticoDTO diagnosticoDTO);
}
