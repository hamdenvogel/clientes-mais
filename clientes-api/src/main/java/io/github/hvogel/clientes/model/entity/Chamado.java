package io.github.hvogel.clientes.model.entity;

import java.io.Serial;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.hvogel.clientes.enums.StatusChamado;
import io.github.hvogel.clientes.infra.IBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "chamado", schema = "meusservicos")
public class Chamado implements IBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 150)
	@NotNull(message = "{campo.chamado.descricao.obrigatorio}")
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
	
	@Column(name = "data", updatable = false)	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	@Column(name = "local_acontecimento", nullable = false, length = 150)
	@NotNull(message = "{campo.chamado.local.obrigatorio}")
	private String localAcontecimento;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false, length = 1)
	private StatusChamado status;

	@PrePersist
	public void prePersist() {
		setData(LocalDate.now());
	}
}
