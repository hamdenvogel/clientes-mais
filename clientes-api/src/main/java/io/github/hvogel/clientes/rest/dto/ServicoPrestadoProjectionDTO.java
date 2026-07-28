package io.github.hvogel.clientes.rest.dto;

public class ServicoPrestadoProjectionDTO {

	private String descricao;
	private Integer id;
	private String nome;
	private String pix;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public String getPix() {
		return pix;
	}

	public void setPix(String pix) {
		this.pix = pix;
	}

	public ServicoPrestadoProjectionDTO() {
	}

	public ServicoPrestadoProjectionDTO(String descricao, Integer id, String nome, String pix) {
		this.descricao = descricao;
		this.id = id;
		this.nome = nome;
		this.pix = pix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((pix == null) ? 0 : pix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServicoPrestadoProjectionDTO other = (ServicoPrestadoProjectionDTO) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (pix == null) {
			if (other.pix != null)
				return false;
		} else if (!pix.equals(other.pix))
			return false;
		return true;
	}

}
