package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class TotalProdutosDTO {
	private long totalProdutos;

	public TotalProdutosDTO() {
	}

	@Generated("SparkTools")
	private TotalProdutosDTO(Builder builder) {
		this.totalProdutos = builder.totalProdutos;
	}

	public long getTotalProdutos() {
		return totalProdutos;
	}

	public void setTotalProdutos(long totalProdutos) {
		this.totalProdutos = totalProdutos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalProdutos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalProdutosDTO other = (TotalProdutosDTO) obj;
		return totalProdutos == other.totalProdutos;
	}

	@Override
	public String toString() {
		return "TotalProdutosDTO [totalProdutos=" + totalProdutos + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private long totalProdutos;

		private Builder() {
		}

		public Builder withTotalProdutos(long totalProdutos) {
			this.totalProdutos = totalProdutos;
			return this;
		}

		public TotalProdutosDTO build() {
			return new TotalProdutosDTO(this);
		}
	}

}
