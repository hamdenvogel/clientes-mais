package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.model.entity.ItemPacote;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.model.repository.ItemPacoteRepository;
import io.github.hvogel.clientes.service.impl.ItemPacoteServiceImpl;

@ExtendWith(MockitoExtension.class)
class ItemPacoteServiceTest {

    @Mock
    private ItemPacoteRepository repository;

    private ItemPacoteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ItemPacoteServiceImpl(repository);
    }

    @Test
    void testSalvar() {
        ItemPacote itemPacote = new ItemPacote();
        when(repository.save(any(ItemPacote.class))).thenReturn(itemPacote);

        ItemPacote result = service.salvar(itemPacote);

        assertNotNull(result);
        verify(repository, times(1)).save(itemPacote);
    }

    @Test
    void testAtualizar() {
        ItemPacote itemPacote = new ItemPacote();
        itemPacote.setId(1);
        when(repository.save(any(ItemPacote.class))).thenReturn(itemPacote);

        ItemPacote result = service.atualizar(itemPacote);

        assertNotNull(result);
        verify(repository, times(1)).save(itemPacote);
    }

    @Test
    void testDeletar() {
        ItemPacote itemPacote = new ItemPacote();
        service.deletar(itemPacote);
        verify(repository, times(1)).delete(itemPacote);
    }

    @Test
    void testObterPorId() {
        ItemPacote itemPacote = new ItemPacote();
        itemPacote.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(itemPacote));

        Optional<ItemPacote> result = service.obterPorId(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testObterPorIdPageable() {
        Page<ItemPacote> page = new PageImpl<>(Arrays.asList(new ItemPacote()));
        when(repository.findByIdItemPacote(anyInt(), any(Pageable.class))).thenReturn(page);

        Page<ItemPacote> result = service.obterPorId(1, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByIdItemPacote(anyInt(), any(Pageable.class));
    }

    @Test
    void testObterTodos() {
        ItemPacote itemPacote = new ItemPacote();
        when(repository.findAllByOrderByPacoteAsc()).thenReturn(Arrays.asList(itemPacote));

        List<ItemPacote> result = service.obterTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByOrderByPacoteAsc();
    }

    @Test
    void testRecuperarTodos() {
        Page<ItemPacote> page = new PageImpl<>(Arrays.asList(new ItemPacote()));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ItemPacote> result = service.recuperarTodos(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testObterPorIdPacote() {
        Page<ItemPacote> page = new PageImpl<>(Arrays.asList(new ItemPacote()));
        when(repository.findByIdPacote(anyInt(), any(Pageable.class))).thenReturn(page);

        Page<ItemPacote> result = service.obterPorIdPacote(1, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findByIdPacote(anyInt(), any(Pageable.class));
    }

    @Test
    void testObterPorPacoteEServicoPrestado() {
        ItemPacote itemPacote = new ItemPacote();
        when(repository.existsByPacoteAndServicoPrestado(anyInt(), anyInt())).thenReturn(Optional.of(itemPacote));

        Optional<ItemPacote> result = service.obterPorPacoteEServicoPrestado(1, 1);

        assertTrue(result.isPresent());
        verify(repository, times(1)).existsByPacoteAndServicoPrestado(1, 1);
    }

    @Test
    void testDeletarPorPacoteEServicoPrestado() {
        service.deletarPorPacoteEServicoPrestado(1, 1);
        verify(repository, times(1)).deletarPorPacoteEServicoPrestado(1, 1);
    }

    @Test
    void testExistsByServicoPrestado() {
        when(repository.existsByServicoPrestado(any(ServicoPrestado.class))).thenReturn(true);

        boolean result = service.existsByServicoPrestado(new ServicoPrestado());

        assertTrue(result);
        verify(repository, times(1)).existsByServicoPrestado(any(ServicoPrestado.class));
    }
}
