package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;

public class DocumentoDTO {
	
	private Integer id;
	private Integer chaveId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getChaveId() {
		return chaveId;
	}
	public void setChaveId(Integer chaveId) {
		this.chaveId = chaveId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(chaveId, id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentoDTO other = (DocumentoDTO) obj;
		return Objects.equals(chaveId, other.chaveId) && Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return "DocumentoDTO [id=" + id + ", chaveId=" + chaveId + "]";
	}	

}
