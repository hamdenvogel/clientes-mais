package io.github.hvogel.clientes.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.hvogel.clientes.enums.EPerfil;
import io.github.hvogel.clientes.model.entity.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
  Optional<Perfil> findByNome(EPerfil nome);
}
