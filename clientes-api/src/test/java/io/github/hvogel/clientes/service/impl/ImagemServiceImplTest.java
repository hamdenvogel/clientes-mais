package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.hvogel.clientes.model.entity.Imagem;
import io.github.hvogel.clientes.model.repository.ImagemRepository;

@ExtendWith(MockitoExtension.class)
class ImagemServiceImplTest {

    @Mock
    private ImagemRepository repository;

    private ImagemServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ImagemServiceImpl(repository);
    }

    @Test
    void testSave() {
        Imagem imagem = new Imagem();
        when(repository.save(imagem)).thenReturn(imagem);

        Imagem result = service.save(imagem);

        assertNotNull(result);
        verify(repository, times(1)).save(imagem);
    }

    @Test
    void testSave_Null() {
        assertThrows(NullPointerException.class, () -> service.save(null));
    }

    @Test
    void testFindByFileName() {
        String fileName = "test.jpg";
        Imagem imagem = new Imagem();
        when(repository.findByFileName(fileName)).thenReturn(imagem);

        Imagem result = service.findByFileName(fileName);

        assertNotNull(result);
        verify(repository, times(1)).findByFileName(fileName);
    }

    @Test
    void testFindByUuid() {
        String uuid = "uuid";
        Imagem imagem = new Imagem();
        when(repository.findByUuid(uuid)).thenReturn(imagem);

        Imagem result = service.findByUuid(uuid);

        assertNotNull(result);
        verify(repository, times(1)).findByUuid(uuid);
    }

    @Test
    void testFindAll() {
        Imagem imagem = new Imagem();
        when(repository.findAllByOrderByFileNameAsc()).thenReturn(Arrays.asList(imagem));

        List<Imagem> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByOrderByFileNameAsc();
    }

    @Test
    void testObterPorId() {
        Integer id = 1;
        Imagem imagem = new Imagem();
        when(repository.findById(id)).thenReturn(Optional.of(imagem));

        Optional<Imagem> result = service.obterPorId(id);

        assertTrue(result.isPresent());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindByChaveIdAndDocumentoId() {
        Integer chaveId = 1;
        Integer documentoId = 1;
        Imagem imagem = new Imagem();
        when(repository.findByChaveIdAndDocumentoId(chaveId, documentoId)).thenReturn(Arrays.asList(imagem));

        List<Imagem> result = service.findByChaveIdAndDocumentoId(chaveId, documentoId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByChaveIdAndDocumentoId(chaveId, documentoId);
    }

    @Test
    void testDeletar() {
        Imagem imagem = new Imagem();
        service.deletar(imagem);
        verify(repository, times(1)).delete(imagem);
    }

    @Test
    void testDeleteByChaveIdAndDocumentoId() {
        Integer chaveId = 1;
        Integer documentoId = 1;
        service.deleteByChaveIdAndDocumentoId(chaveId, documentoId);
        verify(repository, times(1)).deleteByChaveIdAndDocumentoId(chaveId, documentoId);
    }

    @Test
    void testGetByChaveIdAndDocumentoId() {
        Integer chaveId = 1;
        Integer documentoId = 1;
        Imagem imagem = new Imagem();
        when(repository.getByChaveIdAndDocumentoId(chaveId, documentoId)).thenReturn(Optional.of(imagem));

        Optional<Imagem> result = service.getByChaveIdAndDocumentoId(chaveId, documentoId);

        assertTrue(result.isPresent());
        verify(repository, times(1)).getByChaveIdAndDocumentoId(chaveId, documentoId);
    }
}
