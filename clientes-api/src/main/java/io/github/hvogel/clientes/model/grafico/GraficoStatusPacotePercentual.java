package io.github.hvogel.clientes.model.grafico;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import jakarta.annotation.Generated;
import java.util.Collections;


public class GraficoStatusPacotePercentual {
	private List<BigDecimal> iniciadoPercentual;
	private List<BigDecimal> aprovadoPercentual;
	private List<BigDecimal> executandoPercentual;
	private List<BigDecimal> canceladoPercentual;
	private List<BigDecimal> finalizadoPercentual;
	@Generated("SparkTools")
	private GraficoStatusPacotePercentual(Builder builder) {
		this.iniciadoPercentual = builder.iniciadoPercentual;
		this.aprovadoPercentual = builder.aprovadoPercentual;
		this.executandoPercentual = builder.executandoPercentual;
		this.canceladoPercentual = builder.canceladoPercentual;
		this.finalizadoPercentual = builder.finalizadoPercentual;
	}
	public List<BigDecimal> getIniciadoPercentual() {
		return iniciadoPercentual;
	}
	public void setIniciadoPercentual(List<BigDecimal> iniciadoPercentual) {
		this.iniciadoPercentual = iniciadoPercentual;
	}
	public List<BigDecimal> getAprovadoPercentual() {
		return aprovadoPercentual;
	}
	public void setAprovadoPercentual(List<BigDecimal> aprovadoPercentual) {
		this.aprovadoPercentual = aprovadoPercentual;
	}
	public List<BigDecimal> getExecutandoPercentual() {
		return executandoPercentual;
	}
	public void setExecutandoPercentual(List<BigDecimal> executandoPercentual) {
		this.executandoPercentual = executandoPercentual;
	}
	public List<BigDecimal> getCanceladoPercentual() {
		return canceladoPercentual;
	}
	public void setCanceladoPercentual(List<BigDecimal> canceladoPercentual) {
		this.canceladoPercentual = canceladoPercentual;
	}
	public List<BigDecimal> getFinalizadoPercentual() {
		return finalizadoPercentual;
	}
	public void setFinalizadoPercentual(List<BigDecimal> finalizadoPercentual) {
		this.finalizadoPercentual = finalizadoPercentual;
	}
	@Override
	public int hashCode() {
		return Objects.hash(aprovadoPercentual, canceladoPercentual, executandoPercentual, finalizadoPercentual, iniciadoPercentual);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraficoStatusPacotePercentual other = (GraficoStatusPacotePercentual) obj;
		return Objects.equals(aprovadoPercentual, other.aprovadoPercentual) && Objects.equals(canceladoPercentual, other.canceladoPercentual)
				&& Objects.equals(executandoPercentual, other.executandoPercentual) && Objects.equals(finalizadoPercentual, other.finalizadoPercentual)
				&& Objects.equals(iniciadoPercentual, other.iniciadoPercentual);
	}
	@Override
	public String toString() {
		return "GraficoStatusPacotePercentual [iniciadoPercentual=" + iniciadoPercentual + ", aprovadoPercentual=" + aprovadoPercentual
				+ ", executandoPercentual=" + executandoPercentual + ", canceladoPercentual=" + canceladoPercentual + ", finalizadoPercentual=" + finalizadoPercentual
				+ "]";
	}
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}
	@Generated("SparkTools")
	public static final class Builder {
		private List<BigDecimal> iniciadoPercentual = Collections.emptyList();
		private List<BigDecimal> aprovadoPercentual = Collections.emptyList();
		private List<BigDecimal> executandoPercentual = Collections.emptyList();
		private List<BigDecimal> canceladoPercentual = Collections.emptyList();
		private List<BigDecimal> finalizadoPercentual = Collections.emptyList();

		private Builder() {
		}

public Builder withIniciadoPercentual(List<BigDecimal> iniciadoPercentual) {
			this.iniciadoPercentual = iniciadoPercentual;
			return this;
		}

public Builder withAprovadoPercentual(List<BigDecimal> aprovadoPercentual) {
			this.aprovadoPercentual = aprovadoPercentual;
			return this;
		}

public Builder withExecutandoPercentual(List<BigDecimal> executandoPercentual) {
			this.executandoPercentual = executandoPercentual;
			return this;
		}

public Builder withCanceladoPercentual(List<BigDecimal> canceladoPercentual) {
			this.canceladoPercentual = canceladoPercentual;
			return this;
		}

public Builder withFinalizadoPercentual(List<BigDecimal> finalizadoPercentual) {
			this.finalizadoPercentual = finalizadoPercentual;
			return this;
		}

		public GraficoStatusPacotePercentual build() {
			return new GraficoStatusPacotePercentual(this);
		}
	}	
			
}
