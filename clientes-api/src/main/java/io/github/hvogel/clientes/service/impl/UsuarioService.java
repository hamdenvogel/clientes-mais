package io.github.hvogel.clientes.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.exception.SenhaInvalidaException;
import io.github.hvogel.clientes.model.entity.Perfil;
import io.github.hvogel.clientes.model.entity.Usuario;
import io.github.hvogel.clientes.model.repository.PerfilRepository;
import io.github.hvogel.clientes.model.repository.UsuarioRepository;
import io.github.hvogel.clientes.util.Messages;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

	private final PasswordEncoder encoder;
	private final UsuarioRepository repository;
	private final PerfilRepository perfilRepository;

	public Usuario salvar(Usuario usuario) {
		validarDuplicidade(usuario, null);
		if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha e obrigatoria.");
		}
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		usuario.setRoles(resolverPerfis(usuario.getRoles()));
		return repository.save(usuario);
	}

	public List<Usuario> obterTodos() {
		return repository.findAll();
	}

	public Usuario obterPorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.USUARIO_NAO_ENCONTRADO));
	}

	@Transactional
	public Usuario atualizar(Long id, Usuario dados) {
		Usuario atual = obterPorId(id);
		validarDuplicidade(dados, id);

		atual.setUsername(dados.getUsername());
		atual.setEmail(dados.getEmail());
		atual.setCpf(dados.getCpf());
		atual.setTelefone(dados.getTelefone());
		atual.setEndereco(dados.getEndereco());
		atual.setCidade(dados.getCidade());
		atual.setUf(dados.getUf());
		atual.setCep(dados.getCep());
		atual.setAtivo(dados.isAtivo());
		atual.setEmailConfirmed(dados.isEmailConfirmed());
		atual.setRoles(resolverPerfis(dados.getRoles()));

		if (dados.getPassword() != null && !dados.getPassword().isBlank()) {
			atual.setPassword(encoder.encode(dados.getPassword()));
		}

		return repository.save(atual);
	}

	@Transactional
	public void deletar(Long id) {
		Usuario usuario = obterPorId(id);
		repository.delete(usuario);
	}

	public Set<Perfil> resolverPerfis(Set<Perfil> perfisRecebidos) {
		Set<Perfil> perfisResolvidos = new LinkedHashSet<>();
		if (perfisRecebidos == null || perfisRecebidos.isEmpty()) {
			perfisResolvidos.add(buscarPerfil(EPerfil.ROLE_USER));
			return perfisResolvidos;
		}

		for (Perfil perfil : perfisRecebidos) {
			if (perfil == null || perfil.getNome() == null) {
				continue;
			}
			perfisResolvidos.add(buscarPerfil(perfil.getNome()));
		}

		if (perfisResolvidos.isEmpty()) {
			perfisResolvidos.add(buscarPerfil(EPerfil.ROLE_USER));
		}

		return perfisResolvidos;
	}

	private Perfil buscarPerfil(EPerfil nomePerfil) {
		return perfilRepository.findByNome(nomePerfil)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, Messages.ROLE_NOT_FOUND_ERROR));
	}

	private void validarDuplicidade(Usuario usuario, Long idAtualizacao) {
		if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login e obrigatorio.");
		}

		repository.findByUsername(usuario.getUsername()).ifPresent(existente -> {
			if (idAtualizacao == null || !existente.getId().equals(idAtualizacao)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"O usuario " + usuario.getUsername() + " ja existe.");
			}
		});

		if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
			repository.findByEmail(usuario.getEmail()).ifPresent(existente -> {
				if (idAtualizacao == null || !existente.getId().equals(idAtualizacao)) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"O email " + usuario.getEmail() + " ja esta em uso.");
				}
			});
		}

		if (usuario.getCpf() == null || usuario.getCpf().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF e obrigatorio.");
		}

		repository.findByCpf(usuario.getCpf()).ifPresent(existente -> {
			if (idAtualizacao == null || !existente.getId().equals(idAtualizacao)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"O CPF " + usuario.getCpf() + " ja esta em uso.");
			}
		});
	}

	public UserDetails autenticar(Usuario usuario) {
		UserDetails user = loadUserByUsername(usuario.getUsername());
		boolean senhasBatem = encoder.matches(usuario.getPassword(), user.getPassword());

		if (senhasBatem) {
			return user;
		}

		throw new SenhaInvalidaException();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final Logger logger = LoggerFactory.getLogger(UserDetails.class);
		logger.info("init loadUserByUsername {} user", username);
		Usuario usuario = repository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Login não encontrado."));
		logger.info("retrieving loadUserByUsername");

		return User
				.builder()
				.username(usuario.getUsername())
				.password(usuario.getPassword())
				.roles("USER")
				.build();
	}
}
