package io.github.hvogel.clientes.model.entity;

import java.io.Serial;
import java.math.BigDecimal;

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
@Table(name = "solucao", schema = "meusservicos")
public class Solucao implements IBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 150)
	@NotNull(message = "{campo.solucao.descricao.obrigatorio}")
	private String descricao;
	
	@ManyToOne
    @JoinColumn(name = "servico_prestado_id")
    private ServicoPrestado servicoPrestado;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "desconto")
	private BigDecimal desconto;
}
