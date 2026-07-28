package io.github.hvogel.clientes.model.entity;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import io.github.hvogel.clientes.infra.IBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "equipamento", schema = "meusservicos")
public class Equipamento implements IBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 150)
	@NotNull(message = "{campo.equipamento.descricao.obrigatorio}")
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "servico_prestado_id")
	private ServicoPrestado servicoPrestado;

	@Column(name = "marca", length = 50)
	private String marca;

	@Column(name = "modelo", length = 50)
	private String modelo;

	@Column(name = "anoFabricacao")
	private Integer anoFabricacao;

	@Column(name = "anoModelo")
	private Integer anoModelo;
}
