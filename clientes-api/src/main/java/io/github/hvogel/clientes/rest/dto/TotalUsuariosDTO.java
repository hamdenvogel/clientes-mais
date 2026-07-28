package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class TotalUsuariosDTO {
	private long totalUsuarios;

	public TotalUsuariosDTO() {
	}

	@Generated("SparkTools")
	private TotalUsuariosDTO(Builder builder) {
		this.totalUsuarios = builder.totalUsuarios;
	}

	public long getTotalUsuarios() {
		return totalUsuarios;
	}

	public void setTotalUsuarios(long totalUsuarios) {
		this.totalUsuarios = totalUsuarios;
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalUsuarios);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalUsuariosDTO other = (TotalUsuariosDTO) obj;
		return totalUsuarios == other.totalUsuarios;
	}

	@Override
	public String toString() {
		return "TotalUsuariosDTO [totalUsuarios=" + totalUsuarios + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private long totalUsuarios;

		private Builder() {
		}

		public Builder withTotalUsuarios(long totalUsuarios) {
			this.totalUsuarios = totalUsuarios;
			return this;
		}

		public TotalUsuariosDTO build() {
			return new TotalUsuariosDTO(this);
		}
	}

}
