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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.ItemPacote;
import io.github.hvogel.clientes.model.entity.Pacote;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.rest.dto.ItemPacoteDTO;
import io.github.hvogel.clientes.rest.dto.TotalItensPacotesDTO;
import io.github.hvogel.clientes.service.ItemPacoteService;
import io.github.hvogel.clientes.service.PacoteService;
import io.github.hvogel.clientes.service.ServicoPrestadoService;
import io.github.hvogel.clientes.service.TotalItensPacotesService;

import io.github.hvogel.clientes.util.Messages;

@RestController
@RequestMapping("/api/item-pacote")
public class ItemPacoteController {

	private static final String TITULO_INFORMACAO = Messages.MSG_INFORMACAO;
	private static final String PACOTE_INEXISTENTE = Messages.PACOTE_INEXISTENTE;
	private static final String SERVICO_INEXISTENTE = Messages.SERVICO_INEXISTENTE;

	private final ItemPacoteService itempacoteService;
	private final PacoteService pacoteService;
	private final ServicoPrestadoService servicoPrestadoService;
	private final TotalItensPacotesService totalItensPacotesService;

	public ItemPacoteController(ItemPacoteService itempacoteService, PacoteService pacoteService,
			ServicoPrestadoService servicoPrestadoService, TotalItensPacotesService totalItensPacotesService) {
		super();
		this.itempacoteService = itempacoteService;
		this.pacoteService = pacoteService;
		this.servicoPrestadoService = servicoPrestadoService;
		this.totalItensPacotesService = totalItensPacotesService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemPacoteDTO salvar(@RequestBody @Valid ItemPacoteDTO dto) {

		ItemPacote itemPacote = new ItemPacote();

		Integer idPacote = dto.getIdPacote();
		Pacote pacote = pacoteService
				.obterPorId(idPacote)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, PACOTE_INEXISTENTE));

		Integer idServicoPrestado = dto.getIdServicoPrestado();
		ServicoPrestado servicoPrestado = servicoPrestadoService
				.obterPorId(idServicoPrestado)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						SERVICO_INEXISTENTE));

		itempacoteService.obterPorPacoteEServicoPrestado(idPacote, idServicoPrestado)
				.ifPresent(s -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"Pacote " + idPacote + " Serviço (descrição): " + servicoPrestado.getDescricao()
									+ " já cadastrados.");
				});

		itemPacote.setPacote(pacote);
		itemPacote.setServicoPrestado(servicoPrestado);
		try {
			itempacoteService.salvar(itemPacote);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		InfoResponseDTO infoResponseDTO = InfoResponseDTO.builder()
				.withMensagem("Serviço associado ao Pacote com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
		dto.setInfoResponseDTO(infoResponseDTO);
		dto.setId(itemPacote.getId());
		dto.setIdPacote(idPacote);
		dto.setIdServicoPrestado(idServicoPrestado);
		return dto;
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO atualizar(@PathVariable Integer id, @RequestBody @Valid ItemPacoteDTO dto) {

		Integer idPacote = dto.getIdPacote();
		Pacote pacote = pacoteService
				.obterPorId(idPacote)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, PACOTE_INEXISTENTE));

		Integer idServicoPrestado = dto.getIdServicoPrestado();
		ServicoPrestado servicoPrestado = servicoPrestadoService
				.obterPorId(idServicoPrestado)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						SERVICO_INEXISTENTE));

		ItemPacote item = itempacoteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));

		item.setPacote(pacote);
		item.setServicoPrestado(servicoPrestado);
		itempacoteService.atualizar(item);

		return InfoResponseDTO.builder()
				.withMensagem("Item do pacote criado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO deletar(@PathVariable Integer id) {

		ItemPacote item = itempacoteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado."));
		itempacoteService.deletar(item);

		return InfoResponseDTO.builder()
				.withMensagem("Serviço atualizado ao Pacote com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();

	}

	@DeleteMapping("pacote/{idParamPacote}/servico/{idParamServicoPrestado}")
	public InfoResponseDTO deletarPorPacoteEServicoPrestado(@PathVariable Integer idParamPacote,
			@PathVariable Integer idParamServicoPrestado) {

		Integer idPacote = idParamPacote;
		if (pacoteService.obterPorId(idPacote).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PACOTE_INEXISTENTE);
		}

		Integer idServicoPrestado = idParamServicoPrestado;
		if (servicoPrestadoService.obterPorId(idServicoPrestado).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SERVICO_INEXISTENTE);
		}

		ItemPacote item = itempacoteService.obterPorPacoteEServicoPrestado(idPacote, idServicoPrestado)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Itens de Pacote e Serviço Prestado não localizados."));
		itempacoteService.deletarPorPacoteEServicoPrestado(item.getPacote().getId(),
				item.getServicoPrestado().getId());

		return InfoResponseDTO.builder()
				.withMensagem("Item do pacote deletado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@GetMapping("pesquisa-paginada")
	public Page<ItemPacote> list(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina,
			@RequestParam(value = "size", defaultValue = "4") Integer tamanhoPagina,
			@RequestParam(value = "sort", defaultValue = "pacote.id,asc") String[] sort,
			@RequestParam(value = "pacote", required = false) Integer idPacote) {

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
		Page<ItemPacote> itemPacotes = null;

		if (idPacote == null) {
			itemPacotes = itempacoteService.recuperarTodos(pageable);
		} else {
			itemPacotes = itempacoteService.obterPorIdPacote(idPacote, pageable);
		}

		return itemPacotes;
	}

	@GetMapping("total-por-pacote/{idPacote}")
	public TotalItensPacotesDTO obterTotalItensPorPacote(@PathVariable Integer idPacote) {
		return TotalItensPacotesDTO.builder()
				.withTotalItensPacotes(totalItensPacotesService.obterTotalItensPacotes(idPacote))
				.build();
	}

}
