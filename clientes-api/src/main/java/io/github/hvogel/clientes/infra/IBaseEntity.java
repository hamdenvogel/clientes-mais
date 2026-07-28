package io.github.hvogel.clientes.infra;

import java.io.Serializable;

public interface IBaseEntity extends Serializable {
	
	Long getId();

	void setId(Long id);
	
}
