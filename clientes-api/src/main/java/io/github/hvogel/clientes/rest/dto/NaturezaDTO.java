package io.github.hvogel.clientes.rest.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NaturezaDTO implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message = "{campo.natureza.descricao.obrigatorio}")	
	private String descricao;
}
