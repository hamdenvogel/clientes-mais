package io.github.hvogel.clientes.rest.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProdutoDTO {
	
	private Integer id;
	
	@NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;
	
	@NotNull(message = "{campo.preco.obrigatorio}")
    private BigDecimal preco;
	
	private String marca;
	
	private String modelo;
	
	private Integer anoFabricacao;
	
	private Integer anoModelo;
	
	private InfoResponseDTO infoResponseDTO;
}
