package io.github.hvogel.clientes.model.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

import io.github.hvogel.clientes.enums.StatusDocumento;


@Entity
@Table(name = "documento" , schema = "meusservicos")
public class Documento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "descricao")
    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false, length = 1)
	private StatusDocumento status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusDocumento getStatus() {
		return status;
	}

	public void setStatus(StatusDocumento status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descricao, id, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Documento other = (Documento) obj;
		return Objects.equals(descricao, other.descricao) && Objects.equals(id, other.id) && status == other.status;
	}

	@Override
	public String toString() {
		return "Documento [id=" + id + ", descricao=" + descricao + ", status=" + status + "]";
	}	

}
