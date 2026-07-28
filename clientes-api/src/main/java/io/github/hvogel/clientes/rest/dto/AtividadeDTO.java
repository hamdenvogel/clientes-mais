package io.github.hvogel.clientes.rest.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtividadeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotNull(message = "{campo.atividade.descricao.obrigatorio}")	
	private String descricao;
}
