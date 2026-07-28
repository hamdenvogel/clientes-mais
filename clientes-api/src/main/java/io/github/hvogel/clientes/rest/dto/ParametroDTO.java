package io.github.hvogel.clientes.rest.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ParametroDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	private Integer id;	

	@NotNull(message = "{campo.parametro.descricao.obrigatorio}")
	private String descricao;	

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
}
