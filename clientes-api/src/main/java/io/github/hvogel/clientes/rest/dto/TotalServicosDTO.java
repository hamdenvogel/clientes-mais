package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class TotalServicosDTO {
	private long totalServicos;

	public TotalServicosDTO() {
	}

	@Generated("SparkTools")
	private TotalServicosDTO(Builder builder) {
		this.totalServicos = builder.totalServicos;
	}

	public long getTotalServicos() {
		return totalServicos;
	}

	public void setTotalServicos(long totalServicos) {
		this.totalServicos = totalServicos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalServicos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalServicosDTO other = (TotalServicosDTO) obj;
		return totalServicos == other.totalServicos;
	}

	@Override
	public String toString() {
		return "TotalServicosDTO [totalServicos=" + totalServicos + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private long totalServicos;

		private Builder() {
		}

		public Builder withTotalServicos(long totalServicos) {
			this.totalServicos = totalServicos;
			return this;
		}

		public TotalServicosDTO build() {
			return new TotalServicosDTO(this);
		}
	}

}
