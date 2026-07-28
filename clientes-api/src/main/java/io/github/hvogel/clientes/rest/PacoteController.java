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
import org.springframework.security.access.prepost.PreAuthorize;
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
import io.github.hvogel.clientes.model.entity.Pacote;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.rest.dto.PacoteDTO;
import io.github.hvogel.clientes.rest.dto.TotalPacotesDTO;
import io.github.hvogel.clientes.service.PacoteService;
import io.github.hvogel.clientes.service.TotalPacotesService;

import io.github.hvogel.clientes.util.Messages;

@RestController
@RequestMapping("/api/pacote")
public class PacoteController {

	private static final String MSG_INFO = Messages.MSG_INFORMACAO;
	private static final String MSG_PACOTE_NAO_ENCONTRADO = Messages.PACOTE_NAO_ENCONTRADO;

	private final PacoteService pacoteService;
	private final TotalPacotesService totalPacotesService;

	public PacoteController(PacoteService pacoteService, TotalPacotesService totalPacotesService) {
		super();
		this.pacoteService = pacoteService;
		this.totalPacotesService = totalPacotesService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PacoteDTO salvar(@RequestBody @Valid PacoteDTO dto) {

		Pacote pacote = new Pacote();
		pacote.setDataConclusao(dto.getDataConclusao());
		pacote.setDataPrevisao(dto.getDataPrevisao());
		pacote.setDescricao(dto.getDescricao());
		pacote.setJustificativa(dto.getJustificativa());
		pacote.setStatus(dto.getStatus());
		InfoResponseDTO infoResponseDTO = InfoResponseDTO.builder()
				.withMensagem("Pacote criado com sucesso.")
				.withTitulo(MSG_INFO)
				.build();
		try {
			pacoteService.salvar(pacote);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		dto.setInfoResponseDTO(infoResponseDTO);
		dto.setId(pacote.getId());
		return dto;
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO atualizar(@PathVariable Integer id, @RequestBody @Valid PacoteDTO dto) {

		try {
			Pacote pacote = pacoteService.obterPorId(id)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_PACOTE_NAO_ENCONTRADO));

			pacote.setId(id);
			pacote.setData(dto.getData());
			pacote.setDataConclusao(dto.getDataConclusao());
			pacote.setDataPrevisao(dto.getDataPrevisao());
			pacote.setDescricao(dto.getDescricao());
			pacote.setJustificativa(dto.getJustificativa());
			pacote.setStatus(dto.getStatus());
			pacoteService.atualizar(pacote);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return InfoResponseDTO.builder()
				.withMensagem("Pacote atualizado com sucesso.")
				.withTitulo(MSG_INFO)
				.build();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO deletar(@PathVariable Integer id) {
		Pacote pacote = pacoteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_PACOTE_NAO_ENCONTRADO));
		pacoteService.deletar(pacote);

		return InfoResponseDTO.builder()
				.withMensagem("Pacote deletado com sucesso.")
				.withTitulo(MSG_INFO)
				.build();
	}

	@GetMapping
	public List<Pacote> obterTodos() {
		return pacoteService.obterTodos();
	}

	@GetMapping("{id}")
	public Pacote acharPorId(@PathVariable Integer id) {
		return pacoteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_PACOTE_NAO_ENCONTRADO));
	}

	@GetMapping("totalPacotes")
	public TotalPacotesDTO obterTotalPacotes() {
		return TotalPacotesDTO.builder()
				.withTotalPacotes(totalPacotesService.obterTotalPacotes())
				.build();
	}

	@GetMapping("pesquisa-paginada")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public Page<Pacote> list(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina,
			@RequestParam(value = "size", defaultValue = "4") Integer tamanhoPagina,
			@RequestParam(value = "sort", defaultValue = "descricao,asc") String[] sort,
			@RequestParam(value = "id", required = false) Integer idPacote,
			@RequestParam(value = "descricao", required = false) String descricao) {

		List<Order> orders = buildOrders(sort);
		Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(orders));
		Page<Pacote> pacotes = null;

		if (descricao != null && !descricao.trim().isEmpty()) {
			pacotes = pacoteService.pesquisarPelaDescricao(descricao, pageable);
		} else if (idPacote == null) {
			pacotes = pacoteService.recuperarTodos(pageable);
		} else {
			if (pacoteService.obterPorId(idPacote).isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pacote inexistente.");
			}

			pacotes = pacoteService.obterPorId(idPacote, pageable);
		}

		return pacotes;
	}

	private List<Order> buildOrders(String[] sort) {
		List<Order> orders = new ArrayList<>();

		if (sort[0].contains(",")) {
			// Faz o sort em mais de duas colunas.
			// sortOrder="field, direction"
			for (String sortOrder : sort) {
				String[] sortSplit = sortOrder.split(",");
				Direction direction = "asc".equals(sortSplit[1]) ? Direction.ASC : Direction.DESC;
				orders.add(new Order(direction, sortSplit[0]));
			}
		} else {
			// sort=[field, direction]
			Direction direction = "asc".equals(sort[1]) ? Direction.ASC : Direction.DESC;
			orders.add(new Order(direction, sort[0]));
		}

		return orders;
	}

}
