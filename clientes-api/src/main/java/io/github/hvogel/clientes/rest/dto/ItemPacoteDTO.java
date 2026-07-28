package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotNull;


public class ItemPacoteDTO {
	
	private Integer id;
	
	@NotNull(message = "{campo.pacote.obrigatorio}")
	private Integer idPacote;
	
	@NotNull(message = "{campo.servico.prestado.obrigatorio}")
	private Integer idServicoPrestado;
	
	private InfoResponseDTO infoResponseDTO;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdPacote() {
		return idPacote;
	}

	public void setIdPacote(Integer idPacote) {
		this.idPacote = idPacote;
	}

	public Integer getIdServicoPrestado() {
		return idServicoPrestado;
	}

	public void setIdServicoPrestado(Integer idServicoPrestado) {
		this.idServicoPrestado = idServicoPrestado;
	}

	public InfoResponseDTO getInfoResponseDTO() {
		return infoResponseDTO;
	}

	public void setInfoResponseDTO(InfoResponseDTO infoResponseDTO) {
		this.infoResponseDTO = infoResponseDTO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idPacote, idServicoPrestado, infoResponseDTO);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPacoteDTO other = (ItemPacoteDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(idPacote, other.idPacote)
				&& Objects.equals(idServicoPrestado, other.idServicoPrestado)
				&& Objects.equals(infoResponseDTO, other.infoResponseDTO);
	}

	@Override
	public String toString() {
		return "ItemPacoteDTO [id=" + id + ", idPacote=" + idPacote + ", idServicoPrestado=" + idServicoPrestado
				+ ", infoResponseDTO=" + infoResponseDTO + "]";
	}
		
}
