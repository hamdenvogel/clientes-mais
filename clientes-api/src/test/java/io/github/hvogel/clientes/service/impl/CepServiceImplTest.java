package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Endereco;
import io.github.hvogel.clientes.rest.dto.EnderecoDTO;
import io.github.hvogel.clientes.service.ValidadorService;

@ExtendWith(MockitoExtension.class)
class CepServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ValidadorService validadorService;

    @InjectMocks
    private CepServiceImpl service;

    @Test
    void testPesquisarCep_Success() {
        ReflectionTestUtils.setField(service, "viaCepFormat", "https://viacep.com.br/ws/{cep}/json/");
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);

        Endereco endereco = new Endereco();
        endereco.setCep("12345678");
        endereco.setBairro("Centro");
        endereco.setLocalidade("São Paulo");
        endereco.setLogradouro("Rua Teste");
        endereco.setUf("SP");
        endereco.setComplemento("");

        doNothing().when(validadorService).validarCep(anyString());
        when(restTemplate.getForObject(anyString(), eq(Endereco.class), anyMap())).thenReturn(endereco);

        EnderecoDTO result = service.pesquisarCep("12345678");
        assertNotNull(result);
        assertEquals("12345678", result.getCep());
        assertEquals("Centro", result.getBairro());
    }

    @Test
    void testPesquisarCep_InvalidCep() {
        doThrow(new RegraNegocioException("Cep inválido.")).when(validadorService).validarCep(anyString());

        assertThrows(ResponseStatusException.class, () -> service.pesquisarCep("invalid"));
    }

    @Test
    void testPesquisarCep_NullFields() {
        ReflectionTestUtils.setField(service, "viaCepFormat", "https://viacep.com.br/ws/{cep}/json/");
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);

        Endereco endereco = new Endereco();
        endereco.setCep(null);
        endereco.setBairro(null);

        doNothing().when(validadorService).validarCep(anyString());
        when(restTemplate.getForObject(anyString(), eq(Endereco.class), anyMap())).thenReturn(endereco);

        EnderecoDTO result = service.pesquisarCep("12345678");
        assertNotNull(result);
        assertEquals("", result.getCep());
        assertEquals("", result.getBairro());
    }

    @Test
    void testPesquisarCep_NotFound() {
        ReflectionTestUtils.setField(service, "viaCepFormat", "https://viacep.com.br/ws/{cep}/json/");

        doNothing().when(validadorService).validarCep(anyString());
        when(restTemplate.getForObject(anyString(), eq(Endereco.class), anyMap())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> service.pesquisarCep("12345678"));
    }
}
