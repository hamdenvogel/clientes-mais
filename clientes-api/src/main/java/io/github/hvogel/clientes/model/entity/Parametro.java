package io.github.hvogel.clientes.model.entity;

import java.io.Serial;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.hvogel.clientes.infra.IBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "parametro", schema = "meusservicos")
public class Parametro implements IBaseEntity{

    @Serial
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 300)
	@NotNull(message = "{campo.parametro.descricao.obrigatorio}")
	private String descricao;
	
	@Column(name="data", columnDefinition = "DATE")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
}
