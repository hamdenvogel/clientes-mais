package io.github.hvogel.clientes.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hvogel.clientes.rest.dto.EnderecoDTO;
import io.github.hvogel.clientes.service.CepService;


@RestController
@RequestMapping("/api/cep")
public class CepController {
	
	private final CepService cepService;	
			
	public CepController(CepService cepService) {
		super();
		this.cepService = cepService;
	}

	@GetMapping("{cep}")
	public EnderecoDTO pesquisarCep(@PathVariable("cep") String cep){
		
		return cepService.pesquisarCep(cep);
	}

}
