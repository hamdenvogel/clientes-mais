package io.github.hvogel.clientes.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.hvogel.clientes.model.entity.FotoPerfil;

public interface FotoPerfilRepository extends JpaRepository<FotoPerfil, Integer> {

    Optional<FotoPerfil> findFirstByOwnerTypeAndOwnerIdAndAtivaTrueOrderByUpdatedAtDesc(String ownerType,
            Integer ownerId);

    List<FotoPerfil> findByOwnerTypeAndOwnerIdOrderByUpdatedAtDesc(String ownerType, Integer ownerId);

    List<FotoPerfil> findByOwnerTypeAndOwnerIdAndAtivaTrueOrderByUpdatedAtDesc(String ownerType, Integer ownerId);

    @Modifying
    @Query("update FotoPerfil f set f.ativa = false where f.ownerType = :ownerType and f.ownerId = :ownerId and f.ativa = true")
    int desativarFotoAtiva(@Param("ownerType") String ownerType, @Param("ownerId") Integer ownerId);

    @Modifying
    @Query("update FotoPerfil f set f.ativa = false where f.id = :id")
    int desativarPorId(@Param("id") Integer id);
}