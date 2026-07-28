package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import io.github.hvogel.clientes.service.impl.ImagemServiceImpl;

@ExtendWith(MockitoExtension.class)
class ImagemServiceTest {

    @Mock
    private ImagemRepository repository;

    private ImagemServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ImagemServiceImpl(repository);
    }

    @Test
    void testSave_Success() {
        Imagem imagem = new Imagem();
        when(repository.save(any(Imagem.class))).thenReturn(imagem);

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
        Imagem imagem = new Imagem();
        when(repository.findByFileName(anyString())).thenReturn(imagem);

        Imagem result = service.findByFileName("test.jpg");

        assertNotNull(result);
        verify(repository, times(1)).findByFileName("test.jpg");
    }

    @Test
    void testFindByUuid() {
        Imagem imagem = new Imagem();
        when(repository.findByUuid(anyString())).thenReturn(imagem);

        Imagem result = service.findByUuid("uuid");

        assertNotNull(result);
        verify(repository, times(1)).findByUuid("uuid");
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
        Imagem imagem = new Imagem();
        imagem.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(imagem));

        Optional<Imagem> result = service.obterPorId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testFindByChaveIdAndDocumentoId() {
        Imagem imagem = new Imagem();
        when(repository.findByChaveIdAndDocumentoId(anyInt(), anyInt())).thenReturn(Arrays.asList(imagem));

        List<Imagem> result = service.findByChaveIdAndDocumentoId(1, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByChaveIdAndDocumentoId(1, 1);
    }

    @Test
    void testDeletar() {
        Imagem imagem = new Imagem();
        service.deletar(imagem);
        verify(repository, times(1)).delete(imagem);
    }

    @Test
    void testDeleteByChaveIdAndDocumentoId() {
        service.deleteByChaveIdAndDocumentoId(1, 1);
        verify(repository, times(1)).deleteByChaveIdAndDocumentoId(1, 1);
    }

    @Test
    void testGetByChaveIdAndDocumentoId() {
        Imagem imagem = new Imagem();
        when(repository.getByChaveIdAndDocumentoId(anyInt(), anyInt())).thenReturn(Optional.of(imagem));

        Optional<Imagem> result = service.getByChaveIdAndDocumentoId(1, 1);

        assertTrue(result.isPresent());
        verify(repository, times(1)).getByChaveIdAndDocumentoId(1, 1);
    }
}
