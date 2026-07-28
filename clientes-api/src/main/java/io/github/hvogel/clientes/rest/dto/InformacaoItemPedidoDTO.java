package io.github.hvogel.clientes.rest.dto;

import java.math.BigDecimal;
import java.util.Objects;
import jakarta.annotation.Generated;

public class InformacaoItemPedidoDTO {
	private String descricaoProduto;
	private BigDecimal precoUnitario;
	private Integer quantidade;

	public InformacaoItemPedidoDTO() {
	}

	@Generated("SparkTools")
	private InformacaoItemPedidoDTO(Builder builder) {
		this.descricaoProduto = builder.descricaoProduto;
		this.precoUnitario = builder.precoUnitario;
		this.quantidade = builder.quantidade;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descricaoProduto, precoUnitario, quantidade);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InformacaoItemPedidoDTO other = (InformacaoItemPedidoDTO) obj;
		return Objects.equals(descricaoProduto, other.descricaoProduto)
				&& Objects.equals(precoUnitario, other.precoUnitario) && Objects.equals(quantidade, other.quantidade);
	}

	@Override
	public String toString() {
		return "InformacaoItemPedidoDTO [descricaoProduto=" + descricaoProduto + ", precoUnitario=" + precoUnitario
				+ ", quantidade=" + quantidade + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private String descricaoProduto;
		private BigDecimal precoUnitario;
		private Integer quantidade;

		private Builder() {
		}

		public Builder withDescricaoProduto(String descricaoProduto) {
			this.descricaoProduto = descricaoProduto;
			return this;
		}

		public Builder withPrecoUnitario(BigDecimal precoUnitario) {
			this.precoUnitario = precoUnitario;
			return this;
		}

		public Builder withQuantidade(Integer quantidade) {
			this.quantidade = quantidade;
			return this;
		}

		public InformacaoItemPedidoDTO build() {
			return new InformacaoItemPedidoDTO(this);
		}
	}

}
