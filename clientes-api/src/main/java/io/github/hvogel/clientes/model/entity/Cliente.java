package io.github.hvogel.clientes.model.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "cliente", schema = "meusservicos")
public class Cliente implements Serializable {

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

	@Column(name = "cep", length = 12)
	private String cep;

	@Column(name = "endereco", length = 100)
	private String endereco;

	@Column(name = "complemento", length = 50)
	private String complemento;

	@Column(name = "uf", length = 2)
	private String uf;

	@Column(name = "cidade", length = 200)
	private String cidade;

	@Transient
	private String captcha;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getPix() {
		return pix;
	}

	public void setPix(String pix) {
		this.pix = pix;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	@PrePersist
	public void prePersist() {
		setDataCadastro(LocalDate.now());
	}

	@Override
	public int hashCode() {
		return Objects.hash(captcha, cep, cidade, complemento, cpf, dataCadastro, endereco, id, nome, pix, uf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(captcha, other.captcha) && Objects.equals(cep, other.cep)
				&& Objects.equals(cidade, other.cidade) && Objects.equals(complemento, other.complemento)
				&& Objects.equals(cpf, other.cpf) && Objects.equals(dataCadastro, other.dataCadastro)
				&& Objects.equals(endereco, other.endereco) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(pix, other.pix) && Objects.equals(uf, other.uf);
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", dataCadastro=" + dataCadastro + ", pix="
				+ pix + ", cep=" + cep + ", endereco=" + endereco + ", complemento=" + complemento + ", uf=" + uf
				+ ", cidade=" + cidade + ", captcha=" + captcha + "]";
	}

}
