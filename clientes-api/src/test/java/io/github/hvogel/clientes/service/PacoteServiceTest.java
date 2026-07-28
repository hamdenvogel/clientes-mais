package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.model.entity.Pacote;
import io.github.hvogel.clientes.model.repository.PacoteRepository;
import io.github.hvogel.clientes.service.impl.PacoteServiceImpl;

@ExtendWith(MockitoExtension.class)
class PacoteServiceTest {

    @Mock
    private PacoteRepository repository;

    @Mock
    private ValidadorService validadorService;

    private PacoteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PacoteServiceImpl(repository, validadorService);
    }

    @Test
    void testSalvar_Success() {
        Pacote pacote = new Pacote();
        pacote.setDescricao("Pacote Teste");
        pacote.setStatus("ATIVO");

        when(repository.findByDescricao(anyString())).thenReturn(Optional.empty());
        doNothing().when(validadorService).validarTipoPacote(anyString());
        when(repository.save(any(Pacote.class))).thenReturn(pacote);

        Pacote result = service.salvar(pacote);

        assertNotNull(result);
        verify(repository, times(1)).save(pacote);
    }

    @Test
    void testSalvar_DuplicateDescricao() {
        Pacote pacote = new Pacote();
        pacote.setDescricao("Pacote Teste");

        when(repository.findByDescricao(anyString())).thenReturn(Optional.of(new Pacote()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(pacote));
        verify(repository, times(0)).save(any(Pacote.class));
    }

    @Test
    void testAtualizar_Success() {
        Pacote pacote = new Pacote();
        pacote.setId(1);
        pacote.setDescricao("Pacote Teste");
        pacote.setStatus("ATIVO");

        when(repository.findByDescricaoAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        doNothing().when(validadorService).validarTipoPacote(anyString());
        when(repository.save(any(Pacote.class))).thenReturn(pacote);

        Pacote result = service.atualizar(pacote);

        assertNotNull(result);
        verify(repository, times(1)).save(pacote);
    }

    @Test
    void testObterTodos() {
        Pacote pacote = new Pacote();
        when(repository.findAllByOrderByDescricaoAsc()).thenReturn(Arrays.asList(pacote));

        List<Pacote> result = service.obterTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByOrderByDescricaoAsc();
    }
}
