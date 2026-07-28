package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class TotalPrestadoresDTO {
	private long totalPrestadores;

	public TotalPrestadoresDTO() {
	}

	@Generated("SparkTools")
	private TotalPrestadoresDTO(Builder builder) {
		this.totalPrestadores = builder.totalPrestadores;
	}

	public long getTotalPrestadores() {
		return totalPrestadores;
	}

	public void setTotalPrestadores(long totalPrestadores) {
		this.totalPrestadores = totalPrestadores;
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalPrestadores);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalPrestadoresDTO other = (TotalPrestadoresDTO) obj;
		return totalPrestadores == other.totalPrestadores;
	}

	@Override
	public String toString() {
		return "TotalPrestadoresDTO [totalPrestadores=" + totalPrestadores + "]";
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private long totalPrestadores;

		private Builder() {
		}

		public Builder withTotalPrestadores(long totalPrestadores) {
			this.totalPrestadores = totalPrestadores;
			return this;
		}

		public TotalPrestadoresDTO build() {
			return new TotalPrestadoresDTO(this);
		}
	}

}
