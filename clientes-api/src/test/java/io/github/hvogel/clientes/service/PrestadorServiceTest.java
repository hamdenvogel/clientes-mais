package io.github.hvogel.clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.model.repository.PrestadorRepository;
import io.github.hvogel.clientes.service.impl.PrestadorServiceImpl;
import io.github.hvogel.clientes.service.impl.ServicoPrestadoCascadeDeleteService;

@ExtendWith(MockitoExtension.class)
class PrestadorServiceTest {

    @Mock
    private PrestadorRepository repository;

    @Mock
    private GoogleService googleService;

    @Mock
    private ServicoPrestadoCascadeDeleteService servicoPrestadoCascadeDeleteService;

    private PrestadorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PrestadorServiceImpl(repository, servicoPrestadoCascadeDeleteService, googleService);
    }

    @Test
    void testSalvar_Success() {
        Prestador prestador = new Prestador();
        prestador.setNome("Fulano de Tal");
        prestador.setCpf("123.456.789-00");
        prestador.setAvaliacao(5);
        prestador.setCaptcha("valid-captcha");

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Prestador.class))).thenReturn(prestador);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Prestador result = service.salvar(prestador);

        assertNotNull(result);
        assertEquals("12345678900", prestador.getCpf());
        assertEquals("Fulano de tal", prestador.getNome());
        verify(repository, times(1)).save(prestador);
    }

    @Test
    void testSalvar_DuplicateNome() {
        Prestador prestador = new Prestador();
        prestador.setNome("Fulano de Tal");
        prestador.setCpf("123.456.789-00");
        prestador.setAvaliacao(5);

        when(repository.findByNome(anyString())).thenReturn(Optional.of(new Prestador()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(prestador));
        verify(repository, never()).save(any(Prestador.class));
    }

    @Test
    void testSalvar_DuplicateCpf() {
        Prestador prestador = new Prestador();
        prestador.setNome("Fulano de Tal");
        prestador.setCpf("123.456.789-00");
        prestador.setAvaliacao(5);

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.of(new Prestador()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(prestador));
        verify(repository, never()).save(any(Prestador.class));
    }

    @Test
    void testAtualizar_Success() {
        Prestador prestador = new Prestador();
        prestador.setId(1);
        prestador.setNome("Fulano de Tal");
        prestador.setCpf("123.456.789-00");
        prestador.setAvaliacao(5);
        prestador.setCaptcha("valid-captcha");

        when(repository.findByCpfAndIdNot(anyString(), any())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), any())).thenReturn(Optional.empty());
        when(repository.save(any(Prestador.class))).thenReturn(prestador);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Prestador result = service.atualizar(prestador);

        assertNotNull(result);
        verify(repository, times(1)).save(prestador);
    }
}
