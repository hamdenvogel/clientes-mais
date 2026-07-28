package io.github.hvogel.clientes.rest.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

import io.github.hvogel.clientes.service.validation.NotEmptyList;


public class PedidoDTO {
	@NotNull(message = "{campo.servico.prestado.obrigatorio}")
    private Integer servico;

    @NotNull(message = "{campo.total-pedido.obrigatorio}")
    private BigDecimal total;

    @NotEmptyList(message = "{campo.items-pedido.obrigatorio}")
    private List<ItemPedidoDTO> items;

	public Integer getServico() {
		return servico;
	}

	public void setServico(Integer servico) {
		this.servico = servico;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<ItemPedidoDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemPedidoDTO> items) {
		this.items = items;
	}

	@Override
	public int hashCode() {
		return Objects.hash(items, servico, total);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PedidoDTO other = (PedidoDTO) obj;
		return Objects.equals(items, other.items) && Objects.equals(servico, other.servico)
				&& Objects.equals(total, other.total);
	}

	@Override
	public String toString() {
		return "PedidoDTO [servico=" + servico + ", total=" + total + ", items=" + items + "]";
	}
        
}
