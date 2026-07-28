package io.github.hvogel.clientes.rest.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import jakarta.annotation.Generated;
import java.util.Collections;

public class InformacoesPedidoDTO {
	private Integer codigo;
	private BigDecimal total;
	private String dataPedido;
	private String status;
	private List<InformacaoItemPedidoDTO> items;

	public InformacoesPedidoDTO() {
	}

	@Generated("SparkTools")
	private InformacoesPedidoDTO(Builder builder) {
		this.codigo = builder.codigo;
		this.total = builder.total;
		this.dataPedido = builder.dataPedido;
		this.status = builder.status;
		this.items = builder.items;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(String dataPedido) {
		this.dataPedido = dataPedido;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<InformacaoItemPedidoDTO> getItems() {
		return items;
	}

	public void setItems(List<InformacaoItemPedidoDTO> items) {
		this.items = items;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, dataPedido, items, status, total);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InformacoesPedidoDTO other = (InformacoesPedidoDTO) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(dataPedido, other.dataPedido)
				&& Objects.equals(items, other.items) && Objects.equals(status, other.status)
				&& Objects.equals(total, other.total);
	}

	@Override
	public String toString() {
		return "InformacoesPedidoDTO [codigo=" + codigo + ", total=" + total + ", dataPedido=" + dataPedido
				+ ", status=" + status + ", items=" + items + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private Integer codigo;
		private BigDecimal total;
		private String dataPedido;
		private String status;
		private List<InformacaoItemPedidoDTO> items = Collections.emptyList();

		private Builder() {
		}

		public Builder withCodigo(Integer codigo) {
			this.codigo = codigo;
			return this;
		}

		public Builder withTotal(BigDecimal total) {
			this.total = total;
			return this;
		}

		public Builder withDataPedido(String dataPedido) {
			this.dataPedido = dataPedido;
			return this;
		}

		public Builder withStatus(String status) {
			this.status = status;
			return this;
		}

		public Builder withItems(List<InformacaoItemPedidoDTO> items) {
			this.items = items;
			return this;
		}

		public InformacoesPedidoDTO build() {
			return new InformacoesPedidoDTO(this);
		}
	}

}
