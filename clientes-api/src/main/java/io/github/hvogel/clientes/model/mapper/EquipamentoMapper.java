package io.github.hvogel.clientes.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import io.github.hvogel.clientes.model.entity.Equipamento;
import io.github.hvogel.clientes.rest.dto.EquipamentoDTO;

@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EquipamentoMapper {
	
	EquipamentoMapper INSTANCE = Mappers.getMapper(EquipamentoMapper.class);
	
	@Mapping(source = "equipamento.servicoPrestado.id", target = "servicoPrestadoId")
	EquipamentoDTO toDto(Equipamento equipamento);

	@Mapping(source = "equipamentoDTO.servicoPrestadoId", target = "servicoPrestado.id")
	Equipamento toEntity(EquipamentoDTO equipamentoDTO);

}
