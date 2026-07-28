package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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

import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.repository.ClienteRepository;
import io.github.hvogel.clientes.service.GoogleService;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private GoogleService googleService;

    @Mock
    private ServicoPrestadoCascadeDeleteService servicoPrestadoCascadeDeleteService;

    @InjectMocks
    private ClienteServiceImpl service;

    @Test
    void testSalvar_Success() {
        Cliente cliente = new Cliente();
        cliente.setNome("joao silva");
        cliente.setCpf("123.456.789-00");
        cliente.setCep("12.345-678");
        cliente.setCaptcha("captcha-token");

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Cliente.class))).thenReturn(cliente);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Cliente result = service.salvar(cliente);

        assertNotNull(result);
        assertEquals("Joao silva", cliente.getNome()); // Capitalization check
        assertEquals("12345678900", cliente.getCpf()); // Formatting check
        assertEquals("12345678", cliente.getCep()); // Formatting check
        verify(repository).save(cliente);
    }

    @Test
    void testSalvar_DuplicateNome() {
        Cliente cliente = new Cliente();
        cliente.setNome("Joao Silva");
        cliente.setCpf("12345678900");

        when(repository.findByNome(anyString())).thenReturn(Optional.of(new Cliente()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(cliente));
    }

    @Test
    void testSalvar_DuplicateCpf() {
        Cliente cliente = new Cliente();
        cliente.setNome("Joao Silva");
        cliente.setCpf("12345678900");

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.of(new Cliente()));

        assertThrows(ResponseStatusException.class, () -> service.salvar(cliente));
    }

    @Test
    void testSalvar_InvalidCpfLength() {
        Cliente cliente = new Cliente();
        cliente.setNome("Joao Silva");
        cliente.setCpf("12345678900123"); // Too long

        when(repository.findByNome(anyString())).thenReturn(Optional.empty());
        when(repository.findByCpf(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.salvar(cliente));
    }

    @Test
    void testAtualizar_Success() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("joao silva");
        cliente.setCpf("123.456.789-00");
        cliente.setCaptcha("captcha-token");

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.save(any(Cliente.class))).thenReturn(cliente);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Cliente result = service.atualizar(cliente);

        assertNotNull(result);
        assertEquals("Joao silva", cliente.getNome());
        assertEquals("12345678900", cliente.getCpf());
        verify(repository).save(cliente);
    }

    @Test
    void testAtualizar_DuplicateNome() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Joao Silva");
        cliente.setCpf("12345678900");

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.of(new Cliente()));

        assertThrows(ResponseStatusException.class, () -> service.atualizar(cliente));
    }

    @Test
    void testDeletar() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        service.deletar(cliente);
        verify(servicoPrestadoCascadeDeleteService).deleteByClienteId(1);
        verify(repository).delete(cliente);
    }

    @Test
    void testDeletar_ComServicoVinculado() {
        Cliente cliente = new Cliente();
        cliente.setId(1);

        service.deletar(cliente);

        verify(servicoPrestadoCascadeDeleteService).deleteByClienteId(1);
        verify(repository).delete(cliente);
    }

    @Test
    void testObterPorId() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = service.obterPorId(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testObterTodos() {
        when(repository.findAllByOrderByNomeAsc()).thenReturn(Arrays.asList(new Cliente()));
        List<Cliente> result = service.obterTodos();
        assertEquals(1, result.size());
    }

    @Test
    void testPesquisarPeloNome() {
        Page<Cliente> page = new PageImpl<>(Arrays.asList(new Cliente()));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Cliente> result = service.pesquisarPeloNome("Joao", pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testAtualizar_DuplicateCpf() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Joao Silva");
        cliente.setCpf("12345678900");

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.of(new Cliente()));

        assertThrows(ResponseStatusException.class, () -> service.atualizar(cliente));
    }

    @Test
    void testAtualizar_CpfTooLong() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Joao Silva");
        cliente.setCpf("12345678900123");

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.atualizar(cliente));
    }

    @Test
    void testAtualizar_Success_WithCep() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("joao silva");
        cliente.setCpf("123.456.789-00");
        cliente.setCaptcha("captcha-token");
        cliente.setCep("12.345-678");

        when(repository.findByCpfAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.findByNomeAndIdNot(anyString(), anyInt())).thenReturn(Optional.empty());
        when(repository.save(any(Cliente.class))).thenReturn(cliente);
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        Cliente result = service.atualizar(cliente);

        assertNotNull(result);
        assertEquals("12345678", cliente.getCep()); // Verify CEP formatting
        verify(repository).save(cliente);
    }
}
