package io.github.hvogel.clientes.rest.dto;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EquipamentoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	private Long id;

	@NotNull(message = "{campo.equipamento.descricao.obrigatorio}")
	private String descricao;

	private Integer servicoPrestadoId;

	private String marca;

	private String modelo;

	private Integer anoFabricacao;

	private Integer anoModelo;
}
