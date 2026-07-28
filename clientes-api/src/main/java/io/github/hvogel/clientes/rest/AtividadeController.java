package io.github.hvogel.clientes.rest;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.infra.AbstractController;
import io.github.hvogel.clientes.model.entity.Atividade;
import io.github.hvogel.clientes.rest.dto.AtividadeDTO;
import io.github.hvogel.clientes.service.AtividadeService;

@RestController
@RequestMapping("/api/atividade")
public class AtividadeController extends AbstractController<Atividade, @Valid AtividadeDTO, AtividadeService> {

	public AtividadeController(AtividadeService service) {
		super(service);
	}

	@GetMapping("lista-sem-paginacao")
	public List<Atividade> obterListaSemPaginacao() {
		return service.obterAtividades();
	}

	@GetMapping("lista-sem-paginacao/{id}")
	public Atividade obterPorId(@PathVariable Long id) {
		return service.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade não encontrada."));
	}

}
