package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;

public class ItemPedidoDTO {
	private Integer produto;
    private Integer quantidade;
	public Integer getProduto() {
		return produto;
	}
	public void setProduto(Integer produto) {
		this.produto = produto;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	@Override
	public int hashCode() {
		return Objects.hash(produto, quantidade);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedidoDTO other = (ItemPedidoDTO) obj;
		return Objects.equals(produto, other.produto) && Objects.equals(quantidade, other.quantidade);
	}
	@Override
	public String toString() {
		return "ItemPedidoDTO [produto=" + produto + ", quantidade=" + quantidade + "]";
	}
        
}
