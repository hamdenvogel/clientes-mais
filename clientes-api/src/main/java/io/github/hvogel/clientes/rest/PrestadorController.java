package io.github.hvogel.clientes.rest;

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
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.FotoPerfil;
import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.model.entity.Profissao;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.rest.dto.FotoPerfilDTO;
import io.github.hvogel.clientes.rest.dto.PrestadorDTO;
import io.github.hvogel.clientes.rest.dto.TotalPrestadoresDTO;
import io.github.hvogel.clientes.service.FotoPerfilService;
import io.github.hvogel.clientes.service.PrestadorService;
import io.github.hvogel.clientes.service.ProfissaoService;
import io.github.hvogel.clientes.service.RelatorioService;
import io.github.hvogel.clientes.service.TotalPrestadoresService;
import io.github.hvogel.clientes.util.DateUtils;

import io.github.hvogel.clientes.util.Messages;

@RestController
@RequestMapping("/api/prestador")
public class PrestadorController {

	private static final String TITULO_INFORMACAO = Messages.MSG_INFORMACAO;
	private static final String PRESTADOR_NAO_ENCONTRADO = Messages.PRESTADOR_NAO_ENCONTRADO;

	private final PrestadorService prestadorService;
	private final ProfissaoService profissaoService;
	private final FotoPerfilService fotoPerfilService;
	private final TotalPrestadoresService totalPrestadoresService;
	private final RelatorioService relatorioService;

	public PrestadorController(PrestadorService prestadorService, ProfissaoService profissaoService,
			FotoPerfilService fotoPerfilService,
			TotalPrestadoresService totalPrestadoresService, RelatorioService relatorioService) {
		super();
		this.prestadorService = prestadorService;
		this.profissaoService = profissaoService;
		this.fotoPerfilService = fotoPerfilService;
		this.totalPrestadoresService = totalPrestadoresService;
		this.relatorioService = relatorioService;
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public PrestadorDTO salvar(@RequestBody @Valid PrestadorDTO dto) {

		Integer idProfissao = dto.getIdProfissao();
		Profissao profissao = profissaoService
				.obterPorId(idProfissao)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissão Inexistente"));

		Prestador prestador = new Prestador();
		prestador.setAvaliacao(dto.getAvaliacao());
		prestador.setCpf(dto.getCpf());
		prestador.setNome(dto.getNome());
		prestador.setPix(dto.getPix());
		prestador.setCaptcha(dto.getCaptcha());
		prestador.setProfissao(profissao);
		prestador.setEmail(dto.getEmail());
		InfoResponseDTO infoResponseDTO = InfoResponseDTO.builder()
				.withMensagem("Prestador criado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
		try {
			prestadorService.salvar(prestador);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		dto.setInfoResponseDTO(infoResponseDTO);
		dto.setId(prestador.getId());

		return dto;
	}

	@PutMapping("{id}")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO atualizar(@PathVariable Integer id, @RequestBody @Valid PrestadorDTO dto) {

		Integer idProfissao = dto.getIdProfissao();
		Profissao profissao = profissaoService
				.obterPorId(idProfissao)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissão Inexistente"));

		try {
			Prestador prestador = prestadorService.obterPorId(id)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRESTADOR_NAO_ENCONTRADO));

			prestador.setAvaliacao(dto.getAvaliacao());
			prestador.setCaptcha(dto.getCaptcha());
			prestador.setCpf(dto.getCpf());
			prestador.setNome(dto.getNome());
			prestador.setPix(dto.getPix());
			prestador.setProfissao(profissao);
			prestador.setEmail(dto.getEmail());
			prestadorService.atualizar(prestador);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return InfoResponseDTO.builder()
				.withMensagem("Prestador atualizado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO deletar(@PathVariable Integer id) {

		Prestador prestador = prestadorService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRESTADOR_NAO_ENCONTRADO));
		prestadorService.deletar(prestador);

		return InfoResponseDTO.builder()
				.withMensagem("Registro excluído com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@GetMapping
	public List<Prestador> obterTodos() {
		return prestadorService.obterTodos();
	}

	@GetMapping("{id}")
	public Prestador acharPorId(@PathVariable Integer id) {
		return prestadorService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRESTADOR_NAO_ENCONTRADO));
	}

	@GetMapping("pesquisa-paginada")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public Page<Prestador> list(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina,
			@RequestParam(value = "size", defaultValue = "4") Integer tamanhoPagina,
			@RequestParam(value = "sort", defaultValue = "nome,asc") String[] sort,
			@RequestParam(value = "id", required = false) Integer idPrestador,
			@RequestParam(value = "nome", required = false) String nome) {

		List<Order> orders = buildSortOrders(sort);
		Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(orders));
		Page<Prestador> prestadores = null;

		if (nome != null && !nome.trim().isEmpty()) {
			prestadores = prestadorService.pesquisarPeloNome(nome, pageable);
		} else if (idPrestador == null) {
			prestadores = prestadorService.recuperarTodos(pageable);
		} else {
			if (prestadorService.obterPorId(idPrestador).isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, PRESTADOR_NAO_ENCONTRADO);
			}

			prestadores = prestadorService.obterPorId(idPrestador, pageable);
		}

		return prestadores;
	}

	@GetMapping("totalPrestadores")
	public TotalPrestadoresDTO obterTotalPrestadores() {
		return TotalPrestadoresDTO.builder()
				.withTotalPrestadores(totalPrestadoresService.obterTotalPrestadores())
				.build();
	}

	@PostMapping("criteria")
	public Page<Prestador> specification(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina,
			@RequestParam(value = "size", defaultValue = "4") Integer tamanhoPagina,
			@RequestParam(value = "sort", defaultValue = "nome,asc") String[] sort,
			@RequestBody List<SearchCriteria> searchCriteria) {

		List<Order> orders = buildSortOrders(sort);

		Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(orders));
		return prestadorService.executaCriteria(searchCriteria, pageable);
	}

	@GetMapping("/relatorio")
	public ResponseEntity<byte[]> relatorioPrestador(
			@RequestParam(value = "id", required = false, defaultValue = "") Long id,
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

		byte[] relatorioPrestador = relatorioService.gerarRelatorioPrestador(id, dataInicio, dataFim);
		HttpHeaders headers = new HttpHeaders();
		String fileName = "relatorio-prestador.pdf";
		headers.setContentDispositionFormData("inline; filename=\"" + fileName + "\"", fileName);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<>(relatorioPrestador, headers, HttpStatus.OK);
	}

	@PostMapping("/{id}/foto")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public FotoPerfilDTO uploadFoto(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
		prestadorService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRESTADOR_NAO_ENCONTRADO));
		try {
			FotoPerfil fotoPerfil = fotoPerfilService.salvarFoto("PRESTADOR", id, file);
			return new FotoPerfilDTO(fotoPerfil);
		} catch (java.io.IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar foto.", e);
		}
	}

	@GetMapping("/{id}/foto")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<byte[]> obterFoto(@PathVariable Integer id) {
		prestadorService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRESTADOR_NAO_ENCONTRADO));
		FotoPerfil fotoPerfil = fotoPerfilService.obterFotoAtiva("PRESTADOR", id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto nao encontrada."));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(fotoPerfil.getMimeType()));
		headers.setContentLength(fotoPerfil.getData() == null ? 0 : fotoPerfil.getData().length);
		return new ResponseEntity<>(fotoPerfil.getData(), headers, HttpStatus.OK);
	}

	@DeleteMapping("/{id}/foto")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO deletarFoto(@PathVariable Integer id) {
		prestadorService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PRESTADOR_NAO_ENCONTRADO));
		fotoPerfilService.deletarFotoAtiva("PRESTADOR", id);
		return InfoResponseDTO.builder()
				.withMensagem("Foto removida com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	private List<Order> buildSortOrders(String[] sort) {
		List<Order> orders = new ArrayList<>();

		if (sort[0].contains(",")) {
			// Faz o sort em mais de duas colunas.
			// sortOrder="field, direction"
			for (String sortOrder : sort) {
				String[] sortParts = sortOrder.split(",");
				Direction direction = "asc".equals(sortParts[1]) ? Direction.ASC : Direction.DESC;
				orders.add(new Order(direction, sortParts[0]));
			}
		} else {
			// sort=[field, direction] or sort=[field]
			Direction direction = Direction.ASC;
			if (sort.length > 1) {
				direction = "asc".equals(sort[1]) ? Direction.ASC : Direction.DESC;
			}
			orders.add(new Order(direction, sort[0]));
		}

		return orders;
	}

}
