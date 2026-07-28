package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import io.github.hvogel.clientes.model.entity.FotoPerfil;
import io.github.hvogel.clientes.model.repository.FotoPerfilRepository;

@ExtendWith(MockitoExtension.class)
class FotoPerfilServiceImplTest {

    @Mock
    private FotoPerfilRepository repository;

    private FotoPerfilServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FotoPerfilServiceImpl(repository);
    }

    @Test
    void salvarFotoDevePersistirNovaFotoEDesativarAnterior() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "foto-cliente.jpg", "image/jpeg",
                "conteudo-foto".getBytes());

        when(repository.desativarFotoAtiva(anyString(), any())).thenReturn(1);
        when(repository.save(any(FotoPerfil.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FotoPerfil result = service.salvarFoto("cliente", 10, file);

        assertNotNull(result);
        assertEquals("CLIENTE", result.getOwnerType());
        assertEquals(10, result.getOwnerId());
        assertEquals("DB", result.getStorageProvider());
        verify(repository, times(1)).desativarFotoAtiva("CLIENTE", 10);
        verify(repository, times(1)).save(any(FotoPerfil.class));
    }

    @Test
    void obterFotoAtivaDeveNormalizarOwnerType() {
        FotoPerfil foto = new FotoPerfil();
        when(repository.findFirstByOwnerTypeAndOwnerIdAndAtivaTrueOrderByUpdatedAtDesc("PRESTADOR", 20))
                .thenReturn(Optional.of(foto));

        Optional<FotoPerfil> result = service.obterFotoAtiva("prestador", 20);

        assertEquals(Optional.of(foto), result);
        verify(repository, times(1)).findFirstByOwnerTypeAndOwnerIdAndAtivaTrueOrderByUpdatedAtDesc("PRESTADOR", 20);
    }

    @Test
    void salvarFotoDeveRejeitarMimeInvalido() {
        MockMultipartFile file = new MockMultipartFile("file", "foto.txt", "text/plain", "abc".getBytes());

        assertThrows(IllegalArgumentException.class, () -> service.salvarFoto("cliente", 10, file));
    }
}