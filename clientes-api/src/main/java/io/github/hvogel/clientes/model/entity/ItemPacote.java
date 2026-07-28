package io.github.hvogel.clientes.model.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "itempacote", schema = "meusservicos")
public class ItemPacote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_pacote", nullable = false)
	private Pacote pacote;
	
	@ManyToOne
	@JoinColumn(name = "id_servico_prestado", nullable = false)
	private ServicoPrestado servicoPrestado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Pacote getPacote() {
		return pacote;
	}

	public void setPacote(Pacote pacote) {
		this.pacote = pacote;
	}

	public ServicoPrestado getServicoPrestado() {
		return servicoPrestado;
	}

	public void setServicoPrestado(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}

	@Override
	public String toString() {
		return "ItemPacote [id=" + id + ", pacote=" + pacote + ", servicoPrestado=" + servicoPrestado + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, pacote, servicoPrestado);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPacote other = (ItemPacote) obj;
		return Objects.equals(id, other.id) && Objects.equals(pacote, other.pacote)
				&& Objects.equals(servicoPrestado, other.servicoPrestado);
	}	
	
}
