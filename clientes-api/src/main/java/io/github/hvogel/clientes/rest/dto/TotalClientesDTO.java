package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class TotalClientesDTO {
	private long totalClientes;

	public TotalClientesDTO() {
	}

	@Generated("SparkTools")
	private TotalClientesDTO(Builder builder) {
		this.totalClientes = builder.totalClientes;
	}

	public long getTotalClientes() {
		return totalClientes;
	}

	public void setTotalClientes(long totalClientes) {
		this.totalClientes = totalClientes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalClientes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalClientesDTO other = (TotalClientesDTO) obj;
		return totalClientes == other.totalClientes;
	}

	@Override
	public String toString() {
		return "TotalClientesDTO [totalClientes=" + totalClientes + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private long totalClientes;

		private Builder() {
		}

		public Builder withTotalClientes(long totalClientes) {
			this.totalClientes = totalClientes;
			return this;
		}

		public TotalClientesDTO build() {
			return new TotalClientesDTO(this);
		}
	}

}
