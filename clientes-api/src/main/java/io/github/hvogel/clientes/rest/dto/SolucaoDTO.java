package io.github.hvogel.clientes.rest.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolucaoDTO implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotNull(message = "{campo.solucao.descricao.obrigatorio}")
	private String descricao;
	
	private Integer servicoPrestadoId;

	private BigDecimal valor;
	
	private BigDecimal desconto;
}
