package io.github.hvogel.clientes.rest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hvogel.clientes.infra.AbstractController;
import io.github.hvogel.clientes.model.entity.Parametro;
import io.github.hvogel.clientes.rest.dto.ParametroDTO;
import io.github.hvogel.clientes.service.ParametroService;

@RestController
@RequestMapping("/api/parametro")
public class ParametroController extends AbstractController<Parametro, @Valid ParametroDTO, ParametroService> {

	public ParametroController(ParametroService service) {
		super(service);
	}

}
