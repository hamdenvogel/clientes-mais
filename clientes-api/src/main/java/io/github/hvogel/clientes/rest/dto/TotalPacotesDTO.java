package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class TotalPacotesDTO {
	private long totalPacotes;

	public TotalPacotesDTO() {
	}

	@Generated("SparkTools")
	private TotalPacotesDTO(Builder builder) {
		this.totalPacotes = builder.totalPacotes;
	}

	public long getTotalPacotes() {
		return totalPacotes;
	}

	public void setTotalPacotes(long totalPacotes) {
		this.totalPacotes = totalPacotes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalPacotes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TotalPacotesDTO other = (TotalPacotesDTO) obj;
		return totalPacotes == other.totalPacotes;
	}

	@Override
	public String toString() {
		return "TotalPacotesDTO [totalPacotes=" + totalPacotes + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private long totalPacotes;

		private Builder() {
		}

		public Builder withTotalPacotes(long totalPacotes) {
			this.totalPacotes = totalPacotes;
			return this;
		}

		public TotalPacotesDTO build() {
			return new TotalPacotesDTO(this);
		}
	}

}
