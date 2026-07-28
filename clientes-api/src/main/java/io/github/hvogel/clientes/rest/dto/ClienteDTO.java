package io.github.hvogel.clientes.rest.dto;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;


public class ClienteDTO {
	private Integer id;
	
	@NotEmpty(message = "{campo.nome.obrigatorio}")
	private String nome;
	
	@NotNull(message = "{campo.cpf.obrigatorio}")
	@CPF(message="{campo.cpf.invalido}")
	private String cpf;		

	private LocalDate dataCadastro;
	
	private String pix;
	
	private String cep;
	
	private String endereco;
	
	private String complemento;
	
	private String uf;	
	
	private String cidade;
	
	private InfoResponseDTO infoResponseDTO;
	
	@PrePersist
	public void prePersist() {
		setDataCadastro(LocalDate.now());
	}

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

	public InfoResponseDTO getInfoResponseDTO() {
		return infoResponseDTO;
	}

	public void setInfoResponseDTO(InfoResponseDTO infoResponseDTO) {
		this.infoResponseDTO = infoResponseDTO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cep, cidade, complemento, cpf, dataCadastro, endereco, id, infoResponseDTO, nome, pix, uf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClienteDTO other = (ClienteDTO) obj;
		return Objects.equals(cep, other.cep) && Objects.equals(cidade, other.cidade)
				&& Objects.equals(complemento, other.complemento) && Objects.equals(cpf, other.cpf)
				&& Objects.equals(dataCadastro, other.dataCadastro) && Objects.equals(endereco, other.endereco)
				&& Objects.equals(id, other.id) && Objects.equals(infoResponseDTO, other.infoResponseDTO)
				&& Objects.equals(nome, other.nome) && Objects.equals(pix, other.pix) && Objects.equals(uf, other.uf);
	}

	@Override
	public String toString() {
		return "ClienteDTO [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", dataCadastro=" + dataCadastro + ", pix="
				+ pix + ", cep=" + cep + ", endereco=" + endereco + ", complemento=" + complemento + ", uf=" + uf
				+ ", cidade=" + cidade + ", infoResponseDTO=" + infoResponseDTO + "]";
	}
	
}
