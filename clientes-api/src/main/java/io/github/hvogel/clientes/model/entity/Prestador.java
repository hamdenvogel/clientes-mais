package io.github.hvogel.clientes.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "prestador", schema = "meusservicos")
public class Prestador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 150)
	@NotEmpty(message = "{campo.nome.obrigatorio}")
	private String nome;

	@Column(nullable = false, length = 11)
	@NotNull(message = "{campo.cpf.obrigatorio}")
	@CPF(message = "{campo.cpf.invalido}")
	private String cpf;

	@Column(name = "data_cadastro", updatable = false)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataCadastro;

	@Column(name = "pix", length = 50)
	private String pix;

	@Column(name = "avaliacao")
	@NotNull(message = "{campo.avaliacao.obrigatorio}")
	@Range(min = 1, max = 5, message = "A avaliação deve estar entre 1 a 5.")
	private Integer avaliacao;

	@ManyToOne
	@JoinColumn(name = "id_profissao")
	@NotNull(message = "{campo.profissao.obrigatorio}")
	private Profissao profissao;

	@Column(name = "email", length = 200)
	private String email;

	@Transient
	private String captcha;

	@PrePersist
	public void prePersist() {
		if (this.dataCadastro == null)
			setDataCadastro(LocalDate.now());
	}
}
