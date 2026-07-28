package io.github.hvogel.clientes.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.br.CPF;

import io.github.hvogel.clientes.service.validation.ExtendedEmail;
import lombok.Data;

@Data
public class PrestadorDTO {
	private Integer id;
	
	@NotEmpty(message = "{campo.nome.obrigatorio}")
	private String nome;
	
	@NotNull(message = "{campo.cpf.obrigatorio}")
	@CPF(message="{campo.cpf.invalido}")
	private String cpf;
	
	private String pix;
	
	@NotNull(message = "{campo.avaliacao.obrigatorio}")
	@Range(min = 1, max = 5, message = "A avaliação deve estar entre 1 a 5.")
	private Integer avaliacao;
	
	@NotNull(message = "{campo.profissao.obrigatorio}")
	private Integer idProfissao;
	
	private String captcha;
	
	@ExtendedEmail
	private String email;
	
	private InfoResponseDTO infoResponseDTO;
}
