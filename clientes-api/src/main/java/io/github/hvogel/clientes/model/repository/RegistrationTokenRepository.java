package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.hvogel.clientes.enums.RegistrationTokenPurpose;
import io.github.hvogel.clientes.model.entity.RegistrationToken;
import io.github.hvogel.clientes.model.entity.Usuario;

public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {

  Optional<RegistrationToken> findByTokenHashAndPurposeAndUsedAtIsNull(String tokenHash, RegistrationTokenPurpose purpose);

  List<RegistrationToken> findByUsuarioAndPurposeAndUsedAtIsNull(Usuario usuario, RegistrationTokenPurpose purpose);
}

