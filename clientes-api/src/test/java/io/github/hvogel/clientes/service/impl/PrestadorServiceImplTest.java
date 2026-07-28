package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.enums.SearchOperation;
import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.model.repository.PrestadorRepository;
import io.github.hvogel.clientes.service.GoogleService;

@ExtendWith(MockitoExtension.class)
class PrestadorServiceImplTest {

    @Mock
    private PrestadorRepository repository;

    @Mock
    private GoogleService googleService;

    @Mock
    private ServicoPrestadoCascadeDeleteService servicoPrestadoCascadeDeleteService;

    @InjectMocks
    private PrestadorServiceImpl service;

    @Test
    void testSalvar_Success() {
        Prestador prestador = new Prestador();
        prestador.setNome("joao prestador");
        prestador.setCpf("123.456.789-00");
        prestador.setAvaliacao(5);
        prestador.setCaptcha("captcha-token");

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Prestador.class))).thenReturn(prestador);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Prestador result = service.salvar(prestador);

        assertNotNull(result);
        assertEquals("Joao prestador", prestador.getNome());
        assertEquals("12345678900", prestador.getCpf());
        verify(repository).save(prestador);
    }

    @Test
    void testSalvar_DuplicateNome() {
        Prestador prestador = new Prestador();
        prestador.setNome("Joao Prestador");
        prestador.setCpf("12345678900");
        prestador.setAvaliacao(5);

        when(repository.findByNome(anyString())).thenReturn(Optional.of(new Prestador()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(prestador));
    }

    @Test
    void testSalvar_DuplicateCpf() {
        Prestador prestador = new Prestador();
        prestador.setNome("Joao Prestador");
        prestador.setCpf("12345678900");
        prestador.setAvaliacao(5);

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.of(new Prestador()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(prestador));
    }

    @Test
    void testSalvar_InvalidAvaliacao() {
        Prestador prestador = new Prestador();
        prestador.setNome("Joao Prestador");
        prestador.setCpf("12345678900");
        prestador.setAvaliacao(0); // Invalid

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.salvar(prestador));
    }

    @Test
    void testAtualizar_Success() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        prestador.setNome("joao prestador");
        prestador.setCpf("123.456.789-00");
        prestador.setAvaliacao(5);
        prestador.setCaptcha("captcha-token");

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.save(any(Prestador.class))).thenReturn(prestador);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Prestador result = service.atualizar(prestador);

        assertNotNull(result);
        assertEquals("Joao prestador", prestador.getNome());
        verify(repository).save(prestador);
    }

    @Test
    void testDeletar() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        service.deletar(prestador);
        verify(servicoPrestadoCascadeDeleteService).deleteByPrestadorId(1);
        verify(repository).delete(prestador);
    }

    @Test
    void testDeletar_ComServicoVinculado() {
        Prestador prestador = new Prestador();
        prestador.setId(1);

        service.deletar(prestador);

        verify(servicoPrestadoCascadeDeleteService).deleteByPrestadorId(1);
        verify(repository).delete(prestador);
    }

    @Test
    void testObterPorId() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(prestador));

        Optional<Prestador> result = service.obterPorId(1);
        assertTrue(result.isPresent());
    }

    @Test
    void testObterTodos() {
        when(repository.findAllByOrderByNomeAsc()).thenReturn(Arrays.asList(new Prestador()));
        List<Prestador> result = service.obterTodos();
        assertEquals(1, result.size());
    }

    @Test
    void testRecuperarTodos() {
        Page<Prestador> page = new PageImpl<>(Arrays.asList(new Prestador()));
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(page);

        Page<Prestador> result = service.recuperarTodos(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testObterPorIdPageable() {
        Page<Prestador> page = new PageImpl<>(Arrays.asList(new Prestador()));
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findByIdPrestador(1, pageable)).thenReturn(page);

        Page<Prestador> result = service.obterPorId(1, pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testPesquisarPeloNome() {
        Page<Prestador> page = new PageImpl<>(Arrays.asList(new Prestador()));
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Prestador> result = service.pesquisarPeloNome("Joao", pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testExecutaCriteria() {
        Page<Prestador> page = new PageImpl<>(Arrays.asList(new Prestador()));
        Pageable pageable = PageRequest.of(0, 10);
        SearchCriteria criteria = new SearchCriteria("nome", "Joao", SearchOperation.LIKE);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Prestador> result = service.executaCriteria(Arrays.asList(criteria), pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testSalvar_AvaliacaoNull() {
        Prestador prestador = new Prestador();
        prestador.setNome("Joao");
        prestador.setCpf("12345678900");
        prestador.setAvaliacao(null);

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.salvar(prestador));
    }

    @Test
    void testSalvar_CpfTooLong() {
        Prestador prestador = new Prestador();
        prestador.setNome("Joao");
        prestador.setCpf("1234567890123"); // 13 chars
        prestador.setAvaliacao(5);

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.salvar(prestador));
    }

    @Test
    void testAtualizar_DuplicateCpf() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        prestador.setNome("Joao");
        prestador.setCpf("12345678900");
        prestador.setAvaliacao(5);

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.of(new Prestador()));

        assertThrows(ResponseStatusException.class, () -> service.atualizar(prestador));
    }

    @Test
    void testAtualizar_DuplicateNome() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        prestador.setNome("Joao");
        prestador.setCpf("12345678900");
        prestador.setAvaliacao(5);

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.of(new Prestador()));

        assertThrows(ResponseStatusException.class, () -> service.atualizar(prestador));
    }

    @Test
    void testAtualizar_CpfTooLong() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        prestador.setNome("Joao");
        prestador.setCpf("1234567890123");
        prestador.setAvaliacao(5);

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.atualizar(prestador));
    }

    @Test
    void testAtualizar_AvaliacaoNull() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        prestador.setNome("Joao");
        prestador.setCpf("12345678900");
        prestador.setAvaliacao(null);

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.atualizar(prestador));
    }

    @Test
    void testAtualizar_AvaliacaoZero() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        prestador.setNome("Joao");
        prestador.setCpf("12345678900");
        prestador.setAvaliacao(0);

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.atualizar(prestador));
    }
}
