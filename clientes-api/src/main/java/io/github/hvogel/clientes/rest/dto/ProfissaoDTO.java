package io.github.hvogel.clientes.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProfissaoDTO {
	
	private Integer id;
	
	@NotEmpty(message = "{campo.profissao.codigo.obrigatorio}")
	private Integer codigo;
	
	@NotEmpty(message = "{campo.profissao.descricao.obrigatorio}")
	private String descricao;
	
	private InfoResponseDTO infoResponseDTO;
}
