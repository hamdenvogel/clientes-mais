package io.github.hvogel.clientes.model.grafico;

import java.util.List;
import java.util.Objects;
import jakarta.annotation.Generated;
import java.util.Collections;

public class GraficoAtendimentoTorta {
	private List<String> statusAtendimento;
	private List<Integer> quantidade;
	@Generated("SparkTools")
	private GraficoAtendimentoTorta(Builder builder) {
		this.statusAtendimento = builder.statusAtendimento;
		this.quantidade = builder.quantidade;
	}
	public List<String> getStatusAtendimento() {
		return statusAtendimento;
	}
	public void setStatusAtendimento(List<String> statusAtendimento) {
		this.statusAtendimento = statusAtendimento;
	}
	public List<Integer> getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(List<Integer> quantidade) {
		this.quantidade = quantidade;
	}
	@Override
	public int hashCode() {
		return Objects.hash(quantidade, statusAtendimento);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraficoAtendimentoTorta other = (GraficoAtendimentoTorta) obj;
		return Objects.equals(quantidade, other.quantidade)
				&& Objects.equals(statusAtendimento, other.statusAtendimento);
	}
	@Override
	public String toString() {
		return "GraficoAtendimentoTorta [statusAtendimento=" + statusAtendimento + ", quantidade=" + quantidade + "]";
	}
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}
	@Generated("SparkTools")
	public static final class Builder {
		private List<String> statusAtendimento = Collections.emptyList();
		private List<Integer> quantidade = Collections.emptyList();

		private Builder() {
		}

		public Builder withStatusAtendimento(List<String> statusAtendimento) {
			this.statusAtendimento = statusAtendimento;
			return this;
		}

		public Builder withQuantidade(List<Integer> quantidade) {
			this.quantidade = quantidade;
			return this;
		}

		public GraficoAtendimentoTorta build() {
			return new GraficoAtendimentoTorta(this);
		}
	} 
			
}
