package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class TotalRegistrosDTO {

	private long total;

	public TotalRegistrosDTO() {
	}

	@Generated("SparkTools")
	private TotalRegistrosDTO(Builder builder) {
		this.total = builder.total;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@Override
	public int hashCode() {
		return Objects.hash(total);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalRegistrosDTO other = (TotalRegistrosDTO) obj;
		return total == other.total;
	}

	@Override
	public String toString() {
		return "TotalRegistrosDTO [total=" + total + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private long total;

		private Builder() {
		}

		public Builder withTotal(long total) {
			this.total = total;
			return this;
		}

		public TotalRegistrosDTO build() {
			return new TotalRegistrosDTO(this);
		}
	}

}
