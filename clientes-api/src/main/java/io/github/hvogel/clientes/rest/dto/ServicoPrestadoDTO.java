package io.github.hvogel.clientes.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import io.github.hvogel.clientes.enums.StatusServico;
import lombok.Data;

@Data
public class ServicoPrestadoDTO {
	
	@NotEmpty(message = "{campo.descricao.obrigatorio}")
	private String descricao;
	
	@NotEmpty(message = "{campo.preco.obrigatorio}")
	private String preco;
	
	@NotEmpty(message = "{campo.data.obrigatorio}")
	private String data;
	
	@NotNull(message = "{campo.cliente.obrigatorio}")
	private Integer idCliente;
	
	private StatusServico status;
	
	private InfoResponseDTO infoResponseDTO;
	
	private String captcha;
	
	private Integer idPrestador;
	
	@NotEmpty(message = "{campo.tipo.servico.obrigatorio}")
	private String tipo;
	
	@NotNull(message = "{campo.natureza.descricao.obrigatorio}")
	private Long idNatureza;
	
	@NotNull(message = "{campo.atividade.descricao.obrigatorio}")
	private Long idAtividade;
	
	private String localAtendimento;
	
	private String conclusao;
}
