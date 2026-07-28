package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class TotalItensPacotesDTO {
	private long totalItensPacotes;

	public TotalItensPacotesDTO() {
	}

	@Generated("SparkTools")
	private TotalItensPacotesDTO(Builder builder) {
		this.totalItensPacotes = builder.totalItensPacotes;
	}

	public long getTotalItensPacotes() {
		return totalItensPacotes;
	}

	public void setTotalItensPacotes(long totalItensPacotes) {
		this.totalItensPacotes = totalItensPacotes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalItensPacotes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalItensPacotesDTO other = (TotalItensPacotesDTO) obj;
		return totalItensPacotes == other.totalItensPacotes;
	}

	@Override
	public String toString() {
		return "TotalItensPacotesDTO [totalItensPacotes=" + totalItensPacotes + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private long totalItensPacotes;

		private Builder() {
		}

		public Builder withTotalItensPacotes(long totalItensPacotes) {
			this.totalItensPacotes = totalItensPacotes;
			return this;
		}

		public TotalItensPacotesDTO build() {
			return new TotalItensPacotesDTO(this);
		}
	}

}
