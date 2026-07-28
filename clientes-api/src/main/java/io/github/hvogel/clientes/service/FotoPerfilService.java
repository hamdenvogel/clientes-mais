package io.github.hvogel.clientes.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import io.github.hvogel.clientes.model.entity.FotoPerfil;

public interface FotoPerfilService {

    FotoPerfil salvarFoto(String ownerType, Integer ownerId, MultipartFile file) throws IOException;

    Optional<FotoPerfil> obterFotoAtiva(String ownerType, Integer ownerId);

    List<FotoPerfil> listarFotos(String ownerType, Integer ownerId);

    Optional<FotoPerfil> obterPorId(Integer id);

    void deletarFotoAtiva(String ownerType, Integer ownerId);
}