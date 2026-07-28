package io.github.hvogel.clientes.rest;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.infra.AbstractController;
import io.github.hvogel.clientes.model.entity.Equipamento;
import io.github.hvogel.clientes.rest.dto.EquipamentoDTO;
import io.github.hvogel.clientes.rest.dto.TotalRegistrosDTO;
import io.github.hvogel.clientes.service.EquipamentoService;

@RestController
@RequestMapping("/api/equipamento")
public class EquipamentoController extends AbstractController<Equipamento, @Valid EquipamentoDTO, EquipamentoService> {

	private static final String EQUIPAMENTO_NAO_ENCONTRADO = "Equipamento não encontrado.";

	public EquipamentoController(EquipamentoService service) {
		super(service);
	}

	@GetMapping("lista-sem-paginacao")
	public List<Equipamento> obterListaSemPaginacao() {
		return service.obterEquipamentos();
	}

	@GetMapping("lista-sem-paginacao/{id}")
	public Equipamento obterPorId(@PathVariable Long id) {
		return service.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EQUIPAMENTO_NAO_ENCONTRADO));
	}

	@GetMapping("total")
	public TotalRegistrosDTO total() {
		return TotalRegistrosDTO.builder()
				.withTotal(this.service.count())
				.build();
	}

	@GetMapping("pesquisa-paginada")
	public Page<Equipamento> list(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina,
			@RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
			@RequestParam(value = "sort", defaultValue = "descricao,asc") String[] sort,
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "id-servico-prestado", required = false) Integer idServicoPrestado) {

		List<Order> orders = new ArrayList<>();

		if (sort[0].contains(",")) {
			// Faz o sort em mais de duas colunas.
			// sortOrder="field, direction"
			Direction direction;
			for (String sortOrder : sort) {
				String[] parts = sortOrder.split(",");

				if (parts[1].equals("asc")) {
					direction = Direction.ASC;
				} else {
					direction = Direction.DESC;
				}

				orders.add(new Order(direction, parts[0]));
			}
		} else {
			// sort=[field, direction]
			Direction direction;
			if (sort[1].equals("asc")) {
				direction = Direction.ASC;
			} else {
				direction = Direction.DESC;
			}

			orders.add(new Order(direction, sort[0]));
		}

		Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(orders));
		Page<Equipamento> equipamento = null;

		if (descricao != null && !descricao.isEmpty() && idServicoPrestado != null) {
			equipamento = service.findByDescricaoContainsAllIgnoreCaseAndServicoPrestadoId(descricao, idServicoPrestado,
					pageable);
		} else if (idServicoPrestado != null) {
			equipamento = service.findByServicoPrestadoId(idServicoPrestado, pageable);
		} else {
			equipamento = service.findAll(pageable);
		}

		return equipamento;
	}

}
