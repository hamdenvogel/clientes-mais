package io.github.hvogel.clientes.model.grafico;

import java.util.List;
import java.util.Objects;
import jakarta.annotation.Generated;
import java.util.Collections;


public class GraficoAtendimentoLinear {
	private List<String> monthYear;
	private List<Integer> emAtendimento; 
	private List<Integer> cancelado; 
	private List<Integer> finalizado;
	@Generated("SparkTools")
	private GraficoAtendimentoLinear(Builder builder) {
		this.monthYear = builder.monthYear;
		this.emAtendimento = builder.emAtendimento;
		this.cancelado = builder.cancelado;
		this.finalizado = builder.finalizado;
	}
	public List<String> getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(List<String> monthYear) {
		this.monthYear = monthYear;
	}
	public List<Integer> getEmAtendimento() {
		return emAtendimento;
	}
	public void setEmAtendimento(List<Integer> emAtendimento) {
		this.emAtendimento = emAtendimento;
	}
	public List<Integer> getCancelado() {
		return cancelado;
	}
	public void setCancelado(List<Integer> cancelado) {
		this.cancelado = cancelado;
	}
	public List<Integer> getFinalizado() {
		return finalizado;
	}
	public void setFinalizado(List<Integer> finalizado) {
		this.finalizado = finalizado;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cancelado, emAtendimento, finalizado, monthYear);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraficoAtendimentoLinear other = (GraficoAtendimentoLinear) obj;
		return Objects.equals(cancelado, other.cancelado) && Objects.equals(emAtendimento, other.emAtendimento)
				&& Objects.equals(finalizado, other.finalizado) && Objects.equals(monthYear, other.monthYear);
	}
	@Override
	public String toString() {
		return "GraficoAtendimentoLinear [monthYear=" + monthYear + ", emAtendimento=" + emAtendimento + ", cancelado="
				+ cancelado + ", finalizado=" + finalizado + "]";
	}
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}
	@Generated("SparkTools")
	public static final class Builder {
		private List<String> monthYear = Collections.emptyList();
		private List<Integer> emAtendimento = Collections.emptyList();
		private List<Integer> cancelado = Collections.emptyList();
		private List<Integer> finalizado = Collections.emptyList();

		private Builder() {
		}

		public Builder withMesAno(List<String> monthYear) {
			this.monthYear = monthYear;
			return this;
		}

		public Builder withEmAtendimento(List<Integer> emAtendimento) {
			this.emAtendimento = emAtendimento;
			return this;
		}

		public Builder withCancelado(List<Integer> cancelado) {
			this.cancelado = cancelado;
			return this;
		}

		public Builder withFinalizado(List<Integer> finalizado) {
			this.finalizado = finalizado;
			return this;
		}

		public GraficoAtendimentoLinear build() {
			return new GraficoAtendimentoLinear(this);
		}
	} 	
		
}
