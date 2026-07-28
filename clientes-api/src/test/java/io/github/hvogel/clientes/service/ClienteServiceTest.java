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

import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.repository.ClienteRepository;
import io.github.hvogel.clientes.service.impl.ClienteServiceImpl;
import io.github.hvogel.clientes.service.impl.ServicoPrestadoCascadeDeleteService;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private GoogleService googleService;

    @Mock
    private ServicoPrestadoCascadeDeleteService servicoPrestadoCascadeDeleteService;

    private ClienteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ClienteServiceImpl(repository, servicoPrestadoCascadeDeleteService, googleService);
    }

    @Test
    void testSalvar_Success() {
        Cliente cliente = new Cliente();
        cliente.setNome("Fulano de Tal");
        cliente.setCpf("123.456.789-00");
        cliente.setCaptcha("valid-captcha");

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Cliente.class))).thenReturn(cliente);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Cliente result = service.salvar(cliente);

        assertNotNull(result);
        assertEquals("12345678900", cliente.getCpf()); // Check formatting
        assertEquals("Fulano de tal", cliente.getNome()); // Check formatting
        verify(repository, times(1)).save(cliente);
    }

    @Test
    void testSalvar_DuplicateNome() {
        Cliente cliente = new Cliente();
        cliente.setNome("Fulano de Tal");
        cliente.setCpf("123.456.789-00");

        when(repository.findByNome(anyString())).thenReturn(Optional.of(new Cliente()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(cliente));
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    void testSalvar_DuplicateCpf() {
        Cliente cliente = new Cliente();
        cliente.setNome("Fulano de Tal");
        cliente.setCpf("123.456.789-00");

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.of(new Cliente()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(cliente));
        verify(repository, never()).save(any(Cliente.class));
    }

    @Test
    void testAtualizar_Success() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Fulano de Tal");
        cliente.setCpf("123.456.789-00");
        cliente.setCaptcha("valid-captcha");

        when(repository.findByCpfAndIdNot(anyString(), any())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), any())).thenReturn(Optional.empty());
        when(repository.save(any(Cliente.class))).thenReturn(cliente);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Cliente result = service.atualizar(cliente);

        assertNotNull(result);
        verify(repository, times(1)).save(cliente);
    }
}
