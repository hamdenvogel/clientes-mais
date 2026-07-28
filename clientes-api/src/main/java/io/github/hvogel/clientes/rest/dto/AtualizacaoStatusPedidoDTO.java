package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;

public class AtualizacaoStatusPedidoDTO {
	private String novoStatus;

	public String getNovoStatus() {
		return novoStatus;
	}

	public void setNovoStatus(String novoStatus) {
		this.novoStatus = novoStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(novoStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtualizacaoStatusPedidoDTO other = (AtualizacaoStatusPedidoDTO) obj;
		return Objects.equals(novoStatus, other.novoStatus);
	}

	@Override
	public String toString() {
		return "AtualizacaoStatusPedidoDTO [novoStatus=" + novoStatus + "]";
	}	
	
}
