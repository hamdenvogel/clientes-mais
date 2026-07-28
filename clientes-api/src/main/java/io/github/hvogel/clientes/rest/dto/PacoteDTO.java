package io.github.hvogel.clientes.rest.dto;

import java.time.LocalDate;

import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class PacoteDTO {
	private Integer id;

	@NotEmpty(message = "{campo.descricao.pacote.obrigatorio}")
	private String descricao;

	@NotEmpty(message = "{campo.justificativa.pacote.obrigatorio}")
	private String justificativa;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataPrevisao;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataConclusao;

	@NotEmpty(message = "{campo.status.pacote.obrigatorio}")
	private String status;

	private InfoResponseDTO infoResponseDTO;

	@PrePersist
	public void prePersist() {
		setData(LocalDate.now());
	}
}
