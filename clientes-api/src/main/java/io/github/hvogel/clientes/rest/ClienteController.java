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
import org.springframework.security.access.prepost.PreAuthorize;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.FotoPerfil;
import io.github.hvogel.clientes.util.Messages;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.rest.dto.ClienteDTO;
import io.github.hvogel.clientes.rest.dto.FotoPerfilDTO;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.rest.dto.ListaNomesDTO;
import io.github.hvogel.clientes.rest.dto.TotalClientesDTO;
import io.github.hvogel.clientes.service.ClienteService;
import io.github.hvogel.clientes.service.FotoPerfilService;
import io.github.hvogel.clientes.service.RelatorioService;
import io.github.hvogel.clientes.service.TotalClientesService;
import io.github.hvogel.clientes.util.DateUtils;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	private final TotalClientesService totalClientesService;
	private final ClienteService clienteService;
	private final FotoPerfilService fotoPerfilService;
	private final RelatorioService relatorioService;

	public ClienteController(TotalClientesService totalClientesService, ClienteService clienteService,
			FotoPerfilService fotoPerfilService, RelatorioService relatorioService) {
		super();
		this.totalClientesService = totalClientesService;
		this.clienteService = clienteService;
		this.fotoPerfilService = fotoPerfilService;
		this.relatorioService = relatorioService;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public List<Cliente> obterTodos() {
		return clienteService.obterTodos();
	}

	@GetMapping("lista-nomes")
	@PreAuthorize("hasAuthority('ROLE_MODERATOR')")
	public List<ListaNomesDTO> obterListaNomeClientes() {
		List<Cliente> listaCliente = clienteService.obterTodos();
		List<String> listaNomes = listaCliente.stream()
				.map(Cliente::getNome).sorted()
				.toList();
		List<ListaNomesDTO> listaNomesDTO = new ArrayList<>();
		listaNomes.forEach(nome -> listaNomesDTO.add(new ListaNomesDTO()));
		return listaNomesDTO;
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteDTO salvar(@RequestBody @Valid Cliente cliente) {
		try {
			Cliente clienteSalvo = clienteService.salvar(cliente);
			InfoResponseDTO infoResponseDTO = InfoResponseDTO.builder()
					.withMensagem("Cliente criado com sucesso.")
					.withTitulo(Messages.MSG_INFORMACAO)
					.build();
			ClienteDTO clienteDTO = new ClienteDTO();
			clienteDTO.setCep(clienteSalvo.getCep());
			clienteDTO.setCidade(clienteSalvo.getCidade());
			clienteDTO.setComplemento(clienteSalvo.getComplemento());
			clienteDTO.setCpf(clienteSalvo.getComplemento());
			clienteDTO.setDataCadastro(clienteSalvo.getDataCadastro());
			clienteDTO.setEndereco(clienteSalvo.getEndereco());
			clienteDTO.setId(clienteSalvo.getId());
			clienteDTO.setInfoResponseDTO(infoResponseDTO);
			clienteDTO.setNome(clienteSalvo.getNome());
			clienteDTO.setPix(clienteSalvo.getPix());
			clienteDTO.setUf(clienteSalvo.getUf());
			return clienteDTO;
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public Cliente acharPorId(@PathVariable Integer id) {
		return clienteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.CLIENTE_NAO_ENCONTRADO));

	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO deletar(@PathVariable Integer id) {
		Cliente cliente = clienteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.CLIENTE_NAO_ENCONTRADO));
		clienteService.deletar(cliente);
		return InfoResponseDTO.builder()
				.withMensagem("Registro excluído com sucesso.")
				.withTitulo(Messages.MSG_INFORMACAO)
				.build();
	}

	@PutMapping("{id}")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO atualizar(@PathVariable Integer id, @RequestBody @Valid Cliente clienteAtualizado) {
		String captcha = clienteAtualizado.getCaptcha();
		try {
			Cliente cliente = clienteService.obterPorId(id)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.CLIENTE_NAO_ENCONTRADO));
			cliente.setNome(clienteAtualizado.getNome());
			cliente.setCpf(clienteAtualizado.getCpf());
			cliente.setCep(clienteAtualizado.getCep());
			cliente.setCidade(clienteAtualizado.getCidade());
			cliente.setComplemento(clienteAtualizado.getComplemento());
			cliente.setEndereco(clienteAtualizado.getEndereco());
			cliente.setPix(clienteAtualizado.getPix());
			cliente.setUf(clienteAtualizado.getUf());
			cliente.setCaptcha(captcha);
			clienteService.atualizar(cliente);
			return InfoResponseDTO.builder()
					.withMensagem("Cliente atualizado com sucesso.")
					.withTitulo(Messages.MSG_INFORMACAO)
					.build();
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("totalClientes")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public TotalClientesDTO obterTotalClientes() {
		return TotalClientesDTO.builder()
				.withTotalClientes(totalClientesService.obterTotalClientes())
				.build();
	}

	@GetMapping("pesquisa-paginada")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public Page<Cliente> list(
			@RequestParam(value = "page", defaultValue = "0") Integer pagina,
			@RequestParam(value = "size", defaultValue = "10") Integer tamanhoPagina,
			@RequestParam(value = "sort", defaultValue = "nome,asc") String[] sort,
			@RequestParam(value = "nome", required = false) String nome) {

		List<Order> orders = new ArrayList<>();

		if (sort[0].contains(",")) {
			for (String sortOrder : sort) {
				String[] parts = sortOrder.split(",");
				if (parts.length > 1) {
					Direction direction = "asc".equalsIgnoreCase(parts[1]) ? Direction.ASC : Direction.DESC;
					orders.add(new Order(direction, parts[0]));
				} else {
					orders.add(new Order(Direction.ASC, parts[0]));
				}
			}
		} else {
			if (sort.length > 1) {
				Direction direction = "asc".equalsIgnoreCase(sort[1]) ? Direction.ASC : Direction.DESC;
				orders.add(new Order(direction, sort[0]));
			} else {
				orders.add(new Order(Direction.ASC, sort[0]));
			}
		}

		// Se nenhum sort válido foi processado, usa padrão
		if (orders.isEmpty()) {
			orders.add(new Order(Direction.ASC, "nome"));
		}

		Pageable pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by(orders));
		Page<Cliente> clientes;
		if (nome != null && !nome.isEmpty()) {
			clientes = clienteService.pesquisarPeloNome(nome, pageable);
		} else {
			clientes = clienteService.recuperarTodos(pageable);
		}

		return clientes;
	}

	@GetMapping("/relatorio")
	public ResponseEntity<byte[]> relatorioCliente(
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

		byte[] relatorioCliente = relatorioService.gerarRelatorioCliente(id, dataInicio, dataFim);
		HttpHeaders headers = new HttpHeaders();
		String fileName = "relatorio-cliente.pdf";
		headers.setContentDispositionFormData("inline; filename=\"" + fileName + "\"", fileName);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<>(relatorioCliente, headers, HttpStatus.OK);
	}

	@PostMapping("/{id}/foto")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public FotoPerfilDTO uploadFoto(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
		clienteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.CLIENTE_NAO_ENCONTRADO));
		try {
			FotoPerfil fotoPerfil = fotoPerfilService.salvarFoto("CLIENTE", id, file);
			return new FotoPerfilDTO(fotoPerfil);
		} catch (java.io.IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar foto.", e);
		}
	}

	@GetMapping("/{id}/foto")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<byte[]> obterFoto(@PathVariable Integer id) {
		clienteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.CLIENTE_NAO_ENCONTRADO));
		FotoPerfil fotoPerfil = fotoPerfilService.obterFotoAtiva("CLIENTE", id)
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
		clienteService.obterPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.CLIENTE_NAO_ENCONTRADO));
		fotoPerfilService.deletarFotoAtiva("CLIENTE", id);
		return InfoResponseDTO.builder()
				.withMensagem("Foto removida com sucesso.")
				.withTitulo(Messages.MSG_INFORMACAO)
				.build();
	}
}
