package io.github.hvogel.clientes.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hvogel.clientes.rest.dto.InfoDTO;
import io.github.hvogel.clientes.service.InfoService;


@RestController
@RequestMapping("/api/info")
public class InfoController {
	private final InfoService infoService;
			
	public InfoController(InfoService infoService) {
		super();
		this.infoService = infoService;
	}

	@GetMapping
	public InfoDTO obterInformacoesApp(){
		return infoService.obterInformacoesAplicacao();		
	}
}
