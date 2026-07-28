package io.github.hvogel.clientes.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Profissao;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.rest.dto.ProfissaoDTO;
import io.github.hvogel.clientes.service.ProfissaoService;

@RestController
@RequestMapping("/api/profissao")
public class ProfissaoController {

	private static final String TITULO_INFORMACAO = "Informação";

	private final ProfissaoService profissaoService;
	private List<Profissao> profissoes = null;

	public ProfissaoController(ProfissaoService profissaoService, List<Profissao> profissoes) {
		super();
		this.profissaoService = profissaoService;
		this.profissoes = profissoes;
	}

	@GetMapping
	public List<Profissao> obterTodos() {
		return profissaoService.obterTodos();
	}

	@GetMapping("{id}")
	public ProfissaoDTO acharPorId(@PathVariable Integer id) {
		try {
			ProfissaoDTO profissaoDTO = new ProfissaoDTO();
			Profissao profissao = profissaoService.obterPorId(id)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissão não encontrada."));

			profissaoDTO.setId(profissao.getId());
			profissaoDTO.setCodigo(profissao.getCodigo());
			profissaoDTO.setDescricao(profissao.getDescricao());
			InfoResponseDTO infoResponseDTO = InfoResponseDTO.builder()
					.withMensagem("Profissão recuperada com sucesso.")
					.withTitulo(TITULO_INFORMACAO)
					.build();
			profissaoDTO.setInfoResponseDTO(infoResponseDTO);
			return profissaoDTO;
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("descricao/{descricao}")
	public List<Profissao> obterPorDescricao(@PathVariable String descricao) {
		try {
			profissoes = profissaoService.obterPorDescricao(descricao.toUpperCase());
		} catch (RegraNegocioException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return profissoes;
	}

}
