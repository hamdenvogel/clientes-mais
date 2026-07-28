package io.github.hvogel.clientes.rest;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.model.entity.Perfil;
import io.github.hvogel.clientes.model.entity.Usuario;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.rest.dto.TotalUsuariosDTO;
import io.github.hvogel.clientes.rest.dto.UsuarioDTO;
import io.github.hvogel.clientes.service.TotalUsuariosService;
import io.github.hvogel.clientes.service.impl.UsuarioService;
import io.github.hvogel.clientes.util.Messages;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private static final String TITULO_INFORMACAO = Messages.MSG_INFORMACAO;

    private final UsuarioService usuarioService;
    private final TotalUsuariosService totalUsuariosService;    
    
	public UsuarioController(UsuarioService usuarioService,
			TotalUsuariosService totalUsuariosService) {
		super();
		this.usuarioService = usuarioService;
		this.totalUsuariosService = totalUsuariosService;
	}

	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO salvar(@RequestBody @Valid UsuarioDTO usuarioDTO ){
		Usuario usuario = toEntity(usuarioDTO);
		Usuario salvo = usuarioService.salvar(usuario);
		UsuarioDTO response = toDTO(salvo);
		response.setInfoResponseDTO(InfoResponseDTO.builder()
				.withMensagem("Usuario criado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build());
		return response;
    }

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioDTO usuarioDTO) {
		Usuario dadosAtualizacao = toEntity(usuarioDTO);
		usuarioService.atualizar(id, dadosAtualizacao);
		return InfoResponseDTO.builder()
				.withMensagem("Usuario atualizado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public InfoResponseDTO deletar(@PathVariable Long id) {
		usuarioService.deletar(id);
		return InfoResponseDTO.builder()
				.withMensagem("Usuario deletado com sucesso.")
				.withTitulo(TITULO_INFORMACAO)
				.build();
	}

	@GetMapping
	public List<UsuarioDTO> obterTodos() {
		return usuarioService.obterTodos()
				.stream()
				.map(this::toDTO)
				.toList();
	}

	@GetMapping("{id}")
	public UsuarioDTO acharPorId(@PathVariable Long id) {
		return toDTO(usuarioService.obterPorId(id));
	}

	@GetMapping("perfis")
	public List<String> listarPerfis() {
		return List.of(EPerfil.ROLE_USER.name(), EPerfil.ROLE_MODERATOR.name(), EPerfil.ROLE_ADMIN.name());
	}
    
    @GetMapping("totalUsuarios")
	public TotalUsuariosDTO obterTotalUsuarios() {
		return TotalUsuariosDTO.builder()
				.withTotalUsuarios(totalUsuariosService.obterTotalUsuarios())
				.build();
	}

	private Usuario toEntity(UsuarioDTO dto) {
		Usuario usuario = new Usuario();
		usuario.setId(dto.getId());
		usuario.setUsername(dto.getUsername());
		usuario.setEmail(dto.getEmail());
		usuario.setPassword(dto.getPassword());
		usuario.setCpf(dto.getCpf());
		usuario.setTelefone(dto.getTelefone());
		usuario.setEndereco(dto.getEndereco());
		usuario.setCidade(dto.getCidade());
		usuario.setUf(dto.getUf());
		usuario.setCep(dto.getCep());
		usuario.setAtivo(dto.isAtivo());
		usuario.setEmailConfirmed(dto.isEmailConfirmed());

		Set<Perfil> perfis = new LinkedHashSet<>();
		if (dto.getRoles() != null) {
			dto.getRoles().forEach(roleName -> {
				if (roleName != null && !roleName.isBlank()) {
					try {
						Perfil perfil = new Perfil();
						perfil.setNome(EPerfil.valueOf(roleName));
						perfis.add(perfil);
					} catch (IllegalArgumentException ex) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Perfil invalido: " + roleName);
					}
				}
			});
		}
		usuario.setRoles(perfis);
		return usuario;
	}

	private UsuarioDTO toDTO(Usuario usuario) {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(usuario.getId());
		dto.setUsername(usuario.getUsername());
		dto.setEmail(usuario.getEmail());
		dto.setCpf(usuario.getCpf());
		dto.setTelefone(usuario.getTelefone());
		dto.setEndereco(usuario.getEndereco());
		dto.setCidade(usuario.getCidade());
		dto.setUf(usuario.getUf());
		dto.setCep(usuario.getCep());
		dto.setAtivo(usuario.isAtivo());
		dto.setEmailConfirmed(usuario.isEmailConfirmed());
		dto.setRoles(usuario.getRoles().stream().map(role -> role.getNome().name()).toList());
		return dto;
	}
}

