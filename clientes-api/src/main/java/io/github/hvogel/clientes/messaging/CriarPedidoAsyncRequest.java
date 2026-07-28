package io.github.hvogel.clientes.messaging;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CriarPedidoAsyncRequest {

	@NotBlank(message = "descricao é obrigatória")
	private String descricao;

	@NotNull(message = "total é obrigatório")
	@DecimalMin(value = "0.01", message = "total deve ser maior que zero")
	private BigDecimal total;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
