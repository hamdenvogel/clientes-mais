package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.data.domain.Example;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.model.repository.ServicoPrestadoRepository;
import io.github.hvogel.clientes.service.GoogleService;
import io.github.hvogel.clientes.service.ValidadorService;

@ExtendWith(MockitoExtension.class)
class ServicoPrestadoServiceImplTest {

    @Mock
    private ServicoPrestadoRepository repository;

    @Mock
    private ValidadorService validadorService;

    @Mock
    private GoogleService googleService;

    @InjectMocks
    private ServicoPrestadoServiceImpl service;

    @Test
    void testSalvar() {
        ServicoPrestado servico = new ServicoPrestado();
        servico.setId(1);

        when(repository.save(any(ServicoPrestado.class))).thenReturn(servico);

        ServicoPrestado result = service.salvar(servico);
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testAtualizar() {
        ServicoPrestado servico = new ServicoPrestado();
        servico.setId(1);

        when(repository.save(any(ServicoPrestado.class))).thenReturn(servico);

        ServicoPrestado result = service.atualizar(servico);
        assertNotNull(result);
    }

    @Test
    void testDeletar() {
        ServicoPrestado servico = new ServicoPrestado();
        assertDoesNotThrow(() -> service.deletar(servico));
        verify(repository).delete(servico);
    }

    @Test
    void testObterPorId() {
        ServicoPrestado servico = new ServicoPrestado();
        servico.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(servico));

        Optional<ServicoPrestado> result = service.obterPorId(1);
        assertTrue(result.isPresent());
    }

    @Test
    void testPesquisarNomeClienteEMes() {
        ServicoPrestado s1 = new ServicoPrestado();
        when(repository.findByNomeClienteAndMes(anyString(), anyInt())).thenReturn(Arrays.asList(s1));

        List<ServicoPrestado> result = service.pesquisarNomeClienteEMes("Cliente", 1);
        assertEquals(1, result.size());
    }

    @Test
    void testIsDataValida() {
        doNothing().when(validadorService).validarData(anyString());
        assertDoesNotThrow(() -> service.isDataValida("01/01/2023"));
    }

    @Test
    void testIsValorValido() {
        doNothing().when(validadorService).validarValorNumerico(anyString());
        assertDoesNotThrow(() -> service.isValorValido("100.00"));
    }

    @Test
    void testExisteDescricaoNoMes() {
        when(repository.existsByDescricaoAndMes(anyString(), anyInt())).thenReturn(Optional.of("desc"));
        Optional<String> result = service.existeDescricaoNoMes("desc", 1);
        assertTrue(result.isPresent());
    }

    @Test
    void testValidaExistenciaDeDescricaoNoMes_ThrowsException() {
        when(repository.existsByDescricaoAndMes(anyString(), anyInt())).thenReturn(Optional.of("desc"));
        assertThrows(RegraNegocioException.class, () -> service.validaExistenciaDeDescricaoNoMes("desc", 1));
    }

    @Test
    void testPesquisarParcialPorDescricao() {
        ServicoPrestado s1 = new ServicoPrestado();
        when(repository.findByDescricaoContainsAllIgnoreCase(anyString())).thenReturn(Arrays.asList(s1));

        List<ServicoPrestado> result = service.pesquisarParcialPorDescricao("test");
        assertEquals(1, result.size());
    }

    @Test
    void testPesquisarParcialPorDescricao_ThrowsException() {
        when(repository.findByDescricaoContainsAllIgnoreCase(anyString())).thenReturn(Arrays.asList());
        assertThrows(RegraNegocioException.class, () -> service.pesquisarParcialPorDescricao("test"));
    }

    @Test
    void testRecuperarTodos() {
        ServicoPrestado s1 = new ServicoPrestado();
        Page<ServicoPrestado> page = new PageImpl<>(Arrays.asList(s1));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<ServicoPrestado> result = service.recuperarTodos(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testPesquisarPorCliente() {
        Cliente cliente = new Cliente();
        ServicoPrestado s1 = new ServicoPrestado();

        when(repository.findByCliente(any(Cliente.class))).thenReturn(Arrays.asList(s1));

        List<ServicoPrestado> result = service.pesquisarPorCliente(cliente);
        assertEquals(1, result.size());
    }

    @Test
    void testExistsByCliente() {
        Cliente cliente = new Cliente();
        when(repository.existsByCliente(any(Cliente.class))).thenReturn(true);
        assertTrue(service.existsByCliente(cliente));
    }

    @Test
    void testMesesGraficoAndOthers() {
        when(repository.mesesGrafico()).thenReturn(Arrays.asList("Jan"));
        when(repository.emAtendimento()).thenReturn(Arrays.asList(1));
        when(repository.finalizado()).thenReturn(Arrays.asList(2));
        when(repository.cancelado()).thenReturn(Arrays.asList(0));
        when(repository.descricaoStatus()).thenReturn(Arrays.asList("Ok"));
        when(repository.quantidadeServicos()).thenReturn(Arrays.asList(5));
        when(repository.tipoUnitario()).thenReturn(Arrays.asList(3));
        when(repository.tipoPacote()).thenReturn(Arrays.asList(4));
        when(repository.mesesGraficoTipoServico()).thenReturn(Arrays.asList("Feb"));

        assertNotNull(service.mesesGrafico());
        assertNotNull(service.emAtendimento());
        assertNotNull(service.finalizado());
        assertNotNull(service.cancelado());
        assertNotNull(service.descricaoStatus());
        assertNotNull(service.quantidadeServicos());
        assertNotNull(service.tipoUnitario());
        assertNotNull(service.tipoPacote());
        assertNotNull(service.mesesGraficoTipoServico());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testPesquisaAvancada() {
        ServicoPrestado sp = new ServicoPrestado();
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<ServicoPrestado> result = service.pesquisaAvancada(sp, pageable);
        assertNotNull(result);
    }

    @Test
    void testValidarValoresIniciais() {
        doNothing().when(validadorService).validarData(anyString());
        doNothing().when(validadorService).validarValorNumerico(anyString());

        when(repository.existsByDescricaoAndMes(anyString(), anyInt())).thenReturn(Optional.empty());
        doNothing().when(validadorService).validarStatus(anyString());
        doNothing().when(validadorService).validarTipoServico(anyString());
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        assertDoesNotThrow(
                () -> service.validarValoresIniciais("01/01/2023", "100", "desc", 1, "status", "captcha", "tipo"));
    }

    @Test
    void testValidarValoresIniciais_DataInvalida() {
        doThrow(new RegraNegocioException("Data inválida")).when(validadorService).validarData(anyString());

        assertThrows(RegraNegocioException.class,
                () -> service.validarValoresIniciais("invalid", "100", "desc", 1, "status", "captcha", "tipo"));
    }

    @Test
    void testValidarValoresIniciais_PrecoInvalido() {
        doNothing().when(validadorService).validarData(anyString());
        doThrow(new RegraNegocioException("Preço inválido")).when(validadorService).validarValorNumerico(anyString());

        assertThrows(RegraNegocioException.class,
                () -> service.validarValoresIniciais("01/01/2023", "invalid", "desc", 1, "status", "captcha", "tipo"));
    }

    @Test
    void testDeletarPorCliente() {
        doNothing().when(repository).deleteByIdCliente(anyInt());
        assertDoesNotThrow(() -> service.deletarPorCliente(1));
        verify(repository).deleteByIdCliente(1);
    }

    @Test
    void testIsTipoValido() {
        doNothing().when(validadorService).validarTipoServico(anyString());
        assertDoesNotThrow(() -> service.isTipoValido("Tipo"));
    }

    @Test
    void testPesquisarPorIdCliente() {
        when(repository.findByIdCliente(anyInt())).thenReturn(Arrays.asList(new ServicoPrestado()));
        List<ServicoPrestado> result = service.pesquisarPorIdCliente(1);
        assertFalse(result.isEmpty());

        when(repository.findByIdCliente(anyInt())).thenReturn(java.util.Collections.emptyList());
        assertThrows(RegraNegocioException.class, () -> service.pesquisarPorIdCliente(1));
    }

    @Test
    void testPesquisarPeloNomeDoCliente() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findByNomeCliente(anyString(), any(Pageable.class))).thenReturn(Page.empty());
        assertNotNull(service.pesquisarPeloNomeDoCliente("nome", pageable));
    }

    @Test
    void testObterServicosAindaNaoVinculados() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.obterServicosAindaNaoVinculados(any(Pageable.class))).thenReturn(Page.empty());
        assertNotNull(service.obterServicosAindaNaoVinculados(pageable));
    }

    @Test
    void testValidarValoresIniciaisAlteracao() {
        doNothing().when(validadorService).validarData(anyString());
        doNothing().when(validadorService).validarValorNumerico(anyString());
        doNothing().when(validadorService).validarStatus(anyString());
        doNothing().when(validadorService).validarTipoServico(anyString());
        doNothing().when(googleService).validarCaptchaPreenchido(anyString());

        assertDoesNotThrow(
                () -> service.validarValoresIniciaisAlteracao("01/01/2023", "100", "status", "captcha", "tipo"));
    }

    @Test
    void testFindAllServicoPrestadoProjectionDTO() {
        when(repository.findAllServicoPrestadoProjectionDTO()).thenReturn(Arrays.asList());
        List<io.github.hvogel.clientes.rest.dto.ServicoPrestadoProjectionDTO> result = service
                .findAllServicoPrestadoProjectionDTO();
        assertNotNull(result);
    }

    @Test
    void testPesquisarPorDescricao_Pageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findByDescricaoContainsAllIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(Page.empty());
        assertNotNull(service.pesquisarPorDescricao("desc", pageable));
    }

    @Test
    void testPesquisarPorIdCliente_Pageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findByIdCliente(anyInt(), any(Pageable.class))).thenReturn(Page.empty());
        assertNotNull(service.pesquisarPorIdCliente(1, pageable));
    }

    @Test
    void testDelegateMethods() {
        when(repository.tipoUnitario()).thenReturn(Arrays.asList(1));
        when(repository.tipoPacote()).thenReturn(Arrays.asList(2));
        assertNotNull(service.tipoUnitario());
        assertNotNull(service.tipoPacote());
    }
}
