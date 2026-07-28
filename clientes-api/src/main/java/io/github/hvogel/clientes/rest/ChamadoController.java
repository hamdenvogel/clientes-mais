package io.github.hvogel.clientes.rest;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.infra.AbstractController;
import io.github.hvogel.clientes.model.entity.Chamado;
import io.github.hvogel.clientes.model.mapper.ChamadoMapper;
import io.github.hvogel.clientes.rest.dto.ChamadoDTO;
import io.github.hvogel.clientes.service.ChamadoService;
import io.github.hvogel.clientes.service.ClienteService;

@RestController
@RequestMapping("/api/chamado")
public class ChamadoController extends AbstractController<Chamado, ChamadoDTO, ChamadoService> {

	private final ClienteService clienteService;
	private final ChamadoMapper chamadoMapper;
	private final ChamadoService chamadoService;

	private static final String CLIENTE_INEXISTENTE = "Cliente inexistente.";
	private static final String CHAMADO_INEXISTENTE = "Chamado inexistente.";

	public ChamadoController(ChamadoService chamadoservice, ClienteService clienteService,
			ChamadoMapper chamadoMapper) {
		super(chamadoservice);
		this.chamadoService = chamadoservice;
		this.clienteService = clienteService;
		this.chamadoMapper = chamadoMapper;
	}

	@PostMapping("salvar")
	@ResponseStatus(HttpStatus.CREATED)
	public ChamadoDTO salvar(@RequestBody @Valid ChamadoDTO chamadoDTO) {

		try {
			if (clienteService.obterPorId(chamadoDTO.getClienteId()).isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_INEXISTENTE);
			}

			Chamado chamado = chamadoMapper.toEntity(chamadoDTO);
			chamado = chamadoService.salvar(chamado);
			return chamadoMapper.toDto(chamado);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("alterar/{id}")
	public ChamadoDTO atualizar(@PathVariable Long id, @RequestBody @Valid ChamadoDTO chamadoDTO) {
		try {
			if (clienteService.obterPorId(chamadoDTO.getClienteId()).isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_INEXISTENTE);
			}

			if (chamadoService.findOneById(id).isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, CHAMADO_INEXISTENTE);
			}

			Chamado chamado = chamadoMapper.toEntity(chamadoDTO);
			chamado = chamadoService.salvar(chamado);
			return chamadoMapper.toDto(chamado);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("deletar/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deletar(@PathVariable Long id) {

		try {
			if (chamadoService.findOneById(id).isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, CHAMADO_INEXISTENTE);
			}
			chamadoService.deleteById(id);
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
