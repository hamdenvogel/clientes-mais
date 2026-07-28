package io.github.hvogel.clientes.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hvogel.clientes.infra.AbstractController;
import io.github.hvogel.clientes.model.entity.Natureza;
import io.github.hvogel.clientes.rest.dto.NaturezaDTO;
import io.github.hvogel.clientes.service.NaturezaService;

@RestController
@RequestMapping("/api/natureza")
public class NaturezaController extends AbstractController<Natureza, NaturezaDTO, NaturezaService> {

	public NaturezaController(NaturezaService service) {
		super(service);
	}
}
