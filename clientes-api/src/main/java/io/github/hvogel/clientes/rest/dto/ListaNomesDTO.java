package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;

public class ListaNomesDTO {
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListaNomesDTO other = (ListaNomesDTO) obj;
		return Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		return "ListaNomesDTO [nome=" + nome + "]";
	}
	
}
