package io.github.hvogel.clientes.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.hvogel.clientes.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Carrega o usuário com seus perfis (roles) em uma única query JOIN FETCH,
	 * evitando LazyInitializationException fora do contexto de transação e
	 * garantindo que as authorities estejam disponíveis no AuthTokenFilter.
	 */
	@EntityGraph(attributePaths = "roles")
	Optional<Usuario> findByUsername(String username);

	Optional<Usuario> findByEmail(String email);

	Optional<Usuario> findByCpf(String cpf);

	boolean existsByUsername(String username);
	long count();
	boolean existsByEmail(String email);
	boolean existsByCpf(String cpf);
}
