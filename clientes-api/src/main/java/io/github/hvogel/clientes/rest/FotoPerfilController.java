package io.github.hvogel.clientes.rest;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.entity.FotoPerfil;
import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.rest.dto.FotoPerfilDTO;
import io.github.hvogel.clientes.rest.dto.InfoResponseDTO;
import io.github.hvogel.clientes.service.ClienteService;
import io.github.hvogel.clientes.service.FotoPerfilService;
import io.github.hvogel.clientes.service.PrestadorService;
import io.github.hvogel.clientes.util.Messages;

@RestController
@RequestMapping("/api/fotos")
public class FotoPerfilController {

    private static final String TITULO_INFORMACAO = Messages.MSG_INFORMACAO;

    private final FotoPerfilService fotoPerfilService;
    private final ClienteService clienteService;
    private final PrestadorService prestadorService;

    public FotoPerfilController(FotoPerfilService fotoPerfilService, ClienteService clienteService,
            PrestadorService prestadorService) {
        this.fotoPerfilService = fotoPerfilService;
        this.clienteService = clienteService;
        this.prestadorService = prestadorService;
    }

    @PostMapping("{ownerType}/{ownerId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FotoPerfilDTO upload(@PathVariable String ownerType, @PathVariable Integer ownerId,
            @RequestParam("file") MultipartFile file) {
        validarDono(ownerType, ownerId);
        try {
            FotoPerfil fotoPerfil = fotoPerfilService.salvarFoto(ownerType, ownerId, file);
            return new FotoPerfilDTO(fotoPerfil);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar foto.", e);
        }
    }

    @GetMapping("{ownerType}/{ownerId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<byte[]> obterFotoAtiva(@PathVariable String ownerType, @PathVariable Integer ownerId) {
        validarDono(ownerType, ownerId);

        FotoPerfil fotoPerfil = fotoPerfilService.obterFotoAtiva(ownerType, ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto nao encontrada."));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(fotoPerfil.getMimeType()));
        headers.setContentLength(fotoPerfil.getData() == null ? 0 : fotoPerfil.getData().length);
        return new ResponseEntity<>(fotoPerfil.getData(), headers, HttpStatus.OK);
    }

    @DeleteMapping("{ownerType}/{ownerId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
    public InfoResponseDTO deletarFotoAtiva(@PathVariable String ownerType, @PathVariable Integer ownerId) {
        validarDono(ownerType, ownerId);
        fotoPerfilService.deletarFotoAtiva(ownerType, ownerId);
        return InfoResponseDTO.builder()
                .withMensagem("Foto removida com sucesso.")
                .withTitulo(TITULO_INFORMACAO)
                .build();
    }

    private void validarDono(String ownerType, Integer ownerId) {
        String ownerTypeNormalizado = ownerType == null ? "" : ownerType.trim().toUpperCase();
        switch (ownerTypeNormalizado) {
            case "CLIENTE" -> clienteService.obterPorId(ownerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.CLIENTE_NAO_ENCONTRADO));
            case "PRESTADOR" -> prestadorService.obterPorId(ownerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Messages.PRESTADOR_NAO_ENCONTRADO));
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ownerType deve ser CLIENTE ou PRESTADOR.");
        }
    }
}