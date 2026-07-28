package io.github.hvogel.clientes.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.github.hvogel.clientes.model.entity.FotoPerfil;
import io.github.hvogel.clientes.model.repository.FotoPerfilRepository;
import io.github.hvogel.clientes.service.FotoPerfilService;

@Service
public class FotoPerfilServiceImpl implements FotoPerfilService {

    private static final List<String> MIME_TYPES_PERMITIDOS = Arrays.asList("image/jpeg", "image/png", "image/webp");

    private final FotoPerfilRepository repository;

    public FotoPerfilServiceImpl(FotoPerfilRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public FotoPerfil salvarFoto(String ownerType, Integer ownerId, MultipartFile file) throws IOException {
        validarEntrada(ownerType, ownerId, file);

        String ownerTypeNormalizado = ownerType.trim().toUpperCase(Locale.ROOT);
        byte[] data = file.getBytes();
        String mimeType = file.getContentType() == null ? "image/jpeg" : file.getContentType();

        repository.desativarFotoAtiva(ownerTypeNormalizado, ownerId);

        FotoPerfil fotoPerfil = new FotoPerfil();
        fotoPerfil.setOwnerType(ownerTypeNormalizado);
        fotoPerfil.setOwnerId(ownerId);
        fotoPerfil.setStorageProvider("DB");
        fotoPerfil.setObjectKey(FotoPerfil.buildObjectKey(ownerTypeNormalizado, ownerId, file));
        fotoPerfil.setFileNameOriginal(file.getOriginalFilename());
        fotoPerfil.setMimeType(mimeType);
        fotoPerfil.setSizeBytes(file.getSize());
        fotoPerfil.setSha256(FotoPerfil.sha256(data));
        fotoPerfil.setData(data);
        fotoPerfil.setAtiva(true);

        return repository.save(fotoPerfil);
    }

    @Override
    public Optional<FotoPerfil> obterFotoAtiva(String ownerType, Integer ownerId) {
        return repository.findFirstByOwnerTypeAndOwnerIdAndAtivaTrueOrderByUpdatedAtDesc(normalizar(ownerType), ownerId);
    }

    @Override
    public List<FotoPerfil> listarFotos(String ownerType, Integer ownerId) {
        return repository.findByOwnerTypeAndOwnerIdOrderByUpdatedAtDesc(normalizar(ownerType), ownerId);
    }

    @Override
    public Optional<FotoPerfil> obterPorId(Integer id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void deletarFotoAtiva(String ownerType, Integer ownerId) {
        obterFotoAtiva(ownerType, ownerId).ifPresent(foto -> repository.desativarPorId(foto.getId()));
    }

    private void validarEntrada(String ownerType, Integer ownerId, MultipartFile file) {
        if (ownerType == null || ownerType.isBlank()) {
            throw new IllegalArgumentException("ownerType e obrigatorio");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId e obrigatorio");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de foto e obrigatorio");
        }
        String mimeType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!MIME_TYPES_PERMITIDOS.contains(mimeType)) {
            throw new IllegalArgumentException("Formato de imagem nao permitido");
        }
    }

    private String normalizar(String ownerType) {
        return ownerType == null ? null : ownerType.trim().toUpperCase(Locale.ROOT);
    }
}