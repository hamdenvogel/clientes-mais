package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class InfoResponseDTO {
	private String titulo;
	private String mensagem;

	@Generated("SparkTools")
	public InfoResponseDTO() {
	}

	@Generated("SparkTools")
	private InfoResponseDTO(Builder builder) {
		this.titulo = builder.titulo;
		this.mensagem = builder.mensagem;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mensagem, titulo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoResponseDTO other = (InfoResponseDTO) obj;
		return Objects.equals(mensagem, other.mensagem) && Objects.equals(titulo, other.titulo);
	}

	@Override
	public String toString() {
		return "InfoResponseDTO [titulo=" + titulo + ", mensagem=" + mensagem + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private String titulo;
		private String mensagem;

		private Builder() {
		}

		public Builder withTitulo(String titulo) {
			this.titulo = titulo;
			return this;
		}

		public Builder withMensagem(String mensagem) {
			this.mensagem = mensagem;
			return this;
		}

		public InfoResponseDTO build() {
			return new InfoResponseDTO(this);
		}
	}

}
