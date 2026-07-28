package io.github.hvogel.clientes.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "pacote", schema = "meusservicos")
public class Pacote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 300)
	private String descricao;

	@Column(nullable = false, length = 300)
	private String justificativa;

	@Column(name = "data", columnDefinition = "DATE", updatable = false)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;

	@Column(name = "data_previsao", columnDefinition = "DATE")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataPrevisao;

	@Column(name = "data_conclusao", columnDefinition = "DATE")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataConclusao;

	@Column(name = "status", nullable = false, length = 1)
	private String status;

	@PrePersist
	public void prePersist() {
		setData(LocalDate.now());
	}
}
