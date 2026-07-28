package io.github.hvogel.clientes.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import io.github.hvogel.clientes.enums.StatusServico;
import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Atividade;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.entity.Natureza;
import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.rest.dto.ServicoPrestadoDTO;
import io.github.hvogel.clientes.rest.dto.ServicoPrestadoProjectionDTO;
import io.github.hvogel.clientes.rest.dto.TotalServicosDTO;
import io.github.hvogel.clientes.service.AtividadeService;
import io.github.hvogel.clientes.service.ClienteService;
import io.github.hvogel.clientes.service.ItemPacoteService;
import io.github.hvogel.clientes.service.NaturezaService;
import io.github.hvogel.clientes.service.PrestadorService;
import io.github.hvogel.clientes.service.RelatorioService;
import io.github.hvogel.clientes.service.ServicoPrestadoService;
import io.github.hvogel.clientes.service.TotalServicosService;
import io.github.hvogel.clientes.service.ValidadorService;
import io.github.hvogel.clientes.util.DateUtils;
import lombok.RequiredArgsConstructor;

import io.github.hvogel.clientes.util.Messages;

@RestController
@RequestMapping("/api/servicos-prestados")
@RequiredArgsConstructor
public class ServicoPrestadoController {

	private final ClienteService clienteService;
	private final TotalServicosService totalServicosService;
	private final ValidadorService validadorService;
	private final ServicoPrestadoService servicoService;
	private final PrestadorService prestadorService;
	private final NaturezaService naturezaService;
	private final ItemPacoteService itemPacoteService;
	private final AtividadeService atividadeService;
	private final RelatorioService relatorioService;

	private static final String TITULO_INFORMACAO = Messages.MSG_INFORMACAO;
	private static final String SERVICO_NAO_ENCONTRADO = Messages.SERVICO_NAO_ENCONTRADO;
	private static final String CLIENTE_INEXISTENTE = Messages.CLIENTE_INEXISTENTE;
	private static final String PRESTADOR_INEXISTENTE = "Prestador inexistente.";
	private static final String NATUREZA_INEXISTENTE = "Natureza inexistente.";
	private static final String ATIVIDADE_INEXISTENTE = "Atividade inexistente.";
	private static final String STATUS_INVALIDO = "Status inválido.";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ServicoPrestadoDTO salvar(@RequestBody @Valid ServicoPrestadoDTO dto) {

		LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		int mes = data.getMonthValue();
		if (dto.getStatus() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, STATUS_INVALIDO);
		}

		try {
			servicoService.validarValoresIniciais(dto.getData(), dto.getPreco(), dto.getDescricao(),
					mes, dto.getStatus().toString(), dto.getCaptcha(), dto.getTipo());
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		Integer idCliente = dto.getIdCliente();

		Cliente cliente = clienteService
				.obterPorId(idCliente)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_INEXISTENTE));

		Prestador prestador = null;
		if (dto.getIdPrestador() != null) {
			Integer idPrestador = dto.getIdPrestador();
			prestador = prestadorService
					.obterPorId(idPrestador)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRESTADOR_INEXISTENTE));
		}

		Natureza natureza = null;
		if (dto.getIdNatureza() != null) {
			Long idNatureza = dto.getIdNatureza();
			natureza = naturezaService
					.obterPorId(idNatureza)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NATUREZA_INEXISTENTE));
		}

		Atividade atividade = null;
		if (dto.getIdAtividade() != null) {
			Long idAtividade = dto.getIdAtividade();
			atividade = atividadeService
					.obterPorId(idAtividade)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ATIVIDADE_INEXISTENTE));
		}

		ServicoPrestado servicoPrestado = new ServicoPrestado();
		servicoPrestado.setDescricao(dto.getDescricao());
		servicoPrestado.setData(data);
		servicoPrestado.setCliente(cliente);
		servicoPrestado.setStatus(dto.getStatus());
		servicoPrestado.setTipo(dto.getTipo());
		servicoPrestado.setValor(validadorService.converter(dto.getPreco()));
		servicoPrestado.setLocalAtendimento(dto.getLocalAtendimento());
		servicoPrestado.setConclusao(dto.getConclusao());

		if (prestador != null) {
			servicoPrestado.setPrestador(prestador);
		}
		if (natureza != null) {
			servicoPrestado.setNatureza(natureza);
		}
		if (atividade != null) {
			servicoPrestado.setAtividade(atividade);
		}

		InfoResponseDTO infoResponseDTO = InfoResponseDTO.builder()
				.withMensagem("Serviço criado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
		servicoService.salvar(servicoPrestado);
		dto.setInfoResponseDTO(infoResponseDTO);
		return dto;
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO atualizar(@PathVariable Integer id, @RequestBody @Valid ServicoPrestadoDTO dto) {

		LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		if (dto.getStatus() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, STATUS_INVALIDO);
		}

		try {
			servicoService.validarValoresIniciaisAlteracao(dto.getData(), dto.getPreco(), dto.getStatus().toString(),
					dto.getCaptcha(), dto.getTipo());
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		Integer idCliente = dto.getIdCliente();

		Cliente cliente = clienteService
				.obterPorId(idCliente)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, CLIENTE_INEXISTENTE));

		Prestador prestador = null;
		if (dto.getIdPrestador() != null) {
			Integer idPrestador = dto.getIdPrestador();
			prestador = prestadorService
					.obterPorId(idPrestador)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, PRESTADOR_INEXISTENTE));
		}

		Natureza natureza = null;
		if (dto.getIdNatureza() != null) {
			Long idNatureza = dto.getIdNatureza();
			natureza = naturezaService
					.findOneById(idNatureza)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, NATUREZA_INEXISTENTE));
		}

		Atividade atividade = null;
		if (dto.getIdAtividade() != null) {
			Long idAtividade = dto.getIdAtividade();
			atividade = atividadeService
					.obterPorId(idAtividade)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ATIVIDADE_INEXISTENTE));
		}

		ServicoPrestado servico = servicoService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SERVICO_NAO_ENCONTRADO));

		servico.setDescricao(dto.getDescricao());
		servico.setData(data);
		servico.setCliente(cliente);
		servico.setStatus(dto.getStatus());
		servico.setTipo(dto.getTipo());
		servico.setValor(validadorService.converter(dto.getPreco()));
		servico.setLocalAtendimento(dto.getLocalAtendimento());
		servico.setConclusao(dto.getConclusao());

		if (dto.getIdPrestador() != null) {
			servico.setPrestador(prestador);
		}
		if (dto.getIdNatureza() != null) {
			servico.setNatureza(natureza);
		}
		if (dto.getIdAtividade() != null) {
			servico.setAtividade(atividade);
		}

		servicoService.atualizar(servico);

		return InfoResponseDTO.builder()
				.withMensagem("Serviço atualizado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO deletar(@PathVariable Integer id) {

		ServicoPrestado servico = servicoService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SERVICO_NAO_ENCONTRADO));

		boolean existemServicos = itemPacoteService.existsByServicoPrestado(servico);
		if (existemServicos) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Este serviço está relacionado em algum pacote. Favor excluir do pacote primeiro.");
		}
		servicoService.deletar(servico);

		return InfoResponseDTO.builder()
				.withMensagem("Serviço deletado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@DeleteMapping("deleta-servicos-cliente/{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO deletarServicosDeUmCliente(@PathVariable Integer id) {

		Cliente cliente = clienteService
				.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, CLIENTE_INEXISTENTE));

		boolean existemServicos = servicoService.existsByCliente(cliente);
		if (!existemServicos) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Não existem serviços cadastrados para este cliente.");
		}
		String nomeCliente = cliente.getNome();
		servicoService.deletarPorCliente(cliente.getId());

		return InfoResponseDTO.builder()
				.withMensagem("Serviços do cliente %s deletados com sucesso.".formatted(nomeCliente))
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@GetMapping("{id}")
	public ServicoPrestado acharPorId(@PathVariable Integer id) {
		return servicoService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, SERVICO_NAO_ENCONTRADO));

	}

	@GetMapping
	public List<ServicoPrestado> pesquisar(
			@RequestParam(value = "nome", required = false, defaultValue = "") String nome,
			@RequestParam(value = "mes", required = false) Integer mes) {

		return servicoService.pesquisarNomeClienteEMes("%" + nome + "%", mes);
	}

	@GetMapping("pesquisa-descricao/{descricao}")
	public List<ServicoPrestado> pesquisarParcialPorDescricao(
			@PathVariable("descricao") String descricao) {
		try {
			return servicoService.pesquisarParcialPorDescricao(descricao);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@GetMapping("totalServicos")
	public TotalServicosDTO obterTotalServicos() {
		return TotalServicosDTO.builder()
				.withTotalServicos(totalServicosService.obterTotalServicos())
				.build();
	}

	@GetMapping("pesquisa-cliente/{id}")
	public List<ServicoPrestado> pesquisarPorCliente(@PathVariable("id") Integer id) {
		Cliente cliente = clienteService
				.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, CLIENTE_INEXISTENTE));

		try {
			return servicoService.pesquisarPorCliente(cliente);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@GetMapping("pesquisa-paginada")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public Page<ServicoPrestado> list(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina,
			@RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
			@RequestParam(value = "sort", defaultValue = "id,asc") String[] sort,
			@RequestParam(value = "id", required = false) Integer idCliente) {

		List<Order> orders = createSortOrders(sort);

		Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(orders));
		Page<ServicoPrestado> servicosPrestados = null;

		if (idCliente == null) {
			servicosPrestados = servicoService.recuperarTodos(pageable);
		} else {
			if (clienteService.obterPorId(idCliente).isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_INEXISTENTE);
			}

			servicosPrestados = servicoService.pesquisarPorIdCliente(idCliente, pageable);
		}

		return servicosPrestados;
	}

	@GetMapping("pesquisa-avancada")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public Page<ServicoPrestado> pesquisaAvancada(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina,
			@RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
			@RequestParam(value = "sort", defaultValue = "id,asc") String[] sort,
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "cliente", required = false) Integer idCliente,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "cliente.nome", required = false) String nomeCliente,
			@RequestParam(value = "orfaos", required = false) String orfaos) {

		List<Order> orders = createSortOrders(sort);

		Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(orders));
		Page<ServicoPrestado> servicosPrestados = null;

		ServicoPrestado servicoPrestadoFiltro = new ServicoPrestado();
		servicoPrestadoFiltro.setDescricao(descricao);

		if (idCliente != null) {
			Cliente cliente = clienteService
					.obterPorId(idCliente)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, CLIENTE_INEXISTENTE));

			servicoPrestadoFiltro.setCliente(cliente);
		}

		if (status != null) {
			servicoPrestadoFiltro.setStatus(StatusServico.valueOf(status));
		}
		if (orfaos != null) {
			servicosPrestados = servicoService.obterServicosAindaNaoVinculados(pageable);
		} else if (nomeCliente == null) {
			servicosPrestados = servicoService.pesquisaAvancada(servicoPrestadoFiltro, pageable);
		} else {
			servicosPrestados = servicoService.pesquisarPeloNomeDoCliente(nomeCliente.toUpperCase(), pageable);
		}

		return servicosPrestados;
	}

	@GetMapping("consulta-lista-servicos")
	public List<ServicoPrestadoProjectionDTO> obterConsultaListaServicos() {
		return servicoService.findAllServicoPrestadoProjectionDTO();
	}

	@GetMapping("/relatorio")
	public ResponseEntity<byte[]> relatorioServicosPrestados(
			@RequestParam(value = "inicio", required = false, defaultValue = "") String inicio,
			@RequestParam(value = "fim", required = false, defaultValue = "") String fim) {
		Date dataInicio = DateUtils.fromString(inicio);
		Date dataFim = DateUtils.fromString(fim, true);

		if (dataInicio == null) {
			dataInicio = DateUtils.getDataInicioPadrao();
		}

		if (dataFim == null) {
			dataFim = DateUtils.hoje(true);
		}

		byte[] relatorioServicosPrestados = relatorioService.gerarRelatorioServicosPrestados(dataInicio, dataFim);
		HttpHeaders headers = new HttpHeaders();
		String fileName = "relatorio-servicos-prestados.pdf";
		headers.setContentDispositionFormData("inline; filename=\"" + fileName + "\"", fileName);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<>(relatorioServicosPrestados, headers, HttpStatus.OK);
	}

	private List<Order> createSortOrders(String[] sort) {
		List<Order> orders = new ArrayList<>();
		if (sort[0].contains(",")) {
			// Faz o sort em mais de duas colunas.
			// sortOrder="field, direction"
			for (String sortOrder : sort) {
				String[] parts = sortOrder.split(",");
				if (parts.length > 1) {
					orders.add(new Order(getSortDirection(parts[1]), parts[0]));
				} else {
					orders.add(new Order(getSortDirection("asc"), parts[0]));
				}
			}
		} else {
			// sort=[field, direction]
			if (sort.length > 1) {
				orders.add(new Order(getSortDirection(sort[1]), sort[0]));
			} else {
				orders.add(new Order(getSortDirection("asc"), sort[0]));
			}
		}
		return orders;
	}

	private Direction getSortDirection(String direction) {
		if ("asc".equals(direction)) {
			return Direction.ASC;
		} else {
			return Direction.DESC;
		}
	}
}
