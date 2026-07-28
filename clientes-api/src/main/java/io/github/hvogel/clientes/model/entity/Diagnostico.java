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
@Table(name = "diagnostico", schema = "meusservicos")
public class Diagnostico implements IBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 150)
	@NotNull(message = "{campo.diagnostico.descricao.obrigatorio}")
	private String descricao;
	
	@ManyToOne
    @JoinColumn(name = "servico_prestado_id")
    private ServicoPrestado servicoPrestado;
}
