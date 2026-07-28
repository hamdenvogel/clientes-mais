package io.github.hvogel.clientes.model.grafico;

import java.util.List;
import java.util.Objects;
import jakarta.annotation.Generated;
import java.util.Collections;

public class GraficoTipoServico {
	private List<String> monthYear;
	private List<Integer> unitario;
	private List<Integer> pacote;
	@Generated("SparkTools")
	private GraficoTipoServico(Builder builder) {
		this.monthYear = builder.monthYear;
		this.unitario = builder.unitario;
		this.pacote = builder.pacote;
	}
	public List<String> getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(List<String> monthYear) {
		this.monthYear = monthYear;
	}
	public List<Integer> getUnitario() {
		return unitario;
	}
	public void setUnitario(List<Integer> unitario) {
		this.unitario = unitario;
	}
	public List<Integer> getPacote() {
		return pacote;
	}
	public void setPacote(List<Integer> pacote) {
		this.pacote = pacote;
	}
	@Override
	public int hashCode() {
		return Objects.hash(monthYear, pacote, unitario);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraficoTipoServico other = (GraficoTipoServico) obj;
		return Objects.equals(monthYear, other.monthYear) && Objects.equals(pacote, other.pacote)
				&& Objects.equals(unitario, other.unitario);
	}
	@Override
	public String toString() {
		return "GraficoTipoServico [monthYear=" + monthYear + ", unitario=" + unitario + ", pacote=" + pacote + "]";
	}
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}
	@Generated("SparkTools")
	public static final class Builder {
		private List<String> monthYear = Collections.emptyList();
		private List<Integer> unitario = Collections.emptyList();
		private List<Integer> pacote = Collections.emptyList();

		private Builder() {
		}

		public Builder withMesAno(List<String> monthYear) {
			this.monthYear = monthYear;
			return this;
		}

		public Builder withUnitario(List<Integer> unitario) {
			this.unitario = unitario;
			return this;
		}

		public Builder withPacote(List<Integer> pacote) {
			this.pacote = pacote;
			return this;
		}

		public GraficoTipoServico build() {
			return new GraficoTipoServico(this);
		}
	}
	
}
