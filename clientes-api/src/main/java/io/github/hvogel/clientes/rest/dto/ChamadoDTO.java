package io.github.hvogel.clientes.rest.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.hvogel.clientes.enums.StatusChamado;
import lombok.Data;

@Data
public class ChamadoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message = "{campo.chamado.descricao.obrigatorio}")
	private String descricao;
	
	private Integer clienteId;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	@NotNull(message = "{campo.chamado.local.obrigatorio}")
	private String localAcontecimento;
	
	private StatusChamado status;
}
