package io.github.hvogel.clientes.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.hvogel.clientes.enums.StatusServico;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "servicoprestado", schema = "meusservicos")
public class ServicoPrestado implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 150)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	@Column
	private BigDecimal valor;

	@Column
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;

	@Column
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataConclusao;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 1)
	private StatusServico status;

	@Transient
	private String captcha;

	@ManyToOne
	@JoinColumn(name = "id_prestador")
	private Prestador prestador;

	@Column(name = "tipo", nullable = false, length = 1)
	private String tipo;

	@ManyToOne
	@JoinColumn(name = "id_natureza")
	private Natureza natureza;

	@ManyToOne
	@JoinColumn(name = "id_atividade")
	private Atividade atividade;

	@Column(name = "local_atendimento", nullable = true, length = 150)
	private String localAtendimento;

	@Column(name = "conclusao", nullable = true, length = 150)
	private String conclusao;
}
