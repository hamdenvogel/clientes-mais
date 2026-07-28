package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import net.sf.jasperreports.engine.JasperRunManager;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceImplTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Resource relatorioPrestador;

    @Mock
    private Resource relatorioCliente;

    @Mock
    private Resource relatorioServicosPrestados;

    @InjectMocks
    private RelatorioServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "relatorioPrestador", relatorioPrestador);
        ReflectionTestUtils.setField(service, "relatorioCliente", relatorioCliente);
        ReflectionTestUtils.setField(service, "relatorioServicosPrestados", relatorioServicosPrestados);
    }

    @Test
    void testGerarRelatorioPrestador() throws SQLException, IOException {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(relatorioPrestador.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        try (MockedStatic<JasperRunManager> mockedJasper = mockStatic(JasperRunManager.class)) {
            mockedJasper.when(
                    () -> JasperRunManager.runReportToPdf(any(InputStream.class), anyMap(), any(Connection.class)))
                    .thenReturn(new byte[10]);

            byte[] result = service.gerarRelatorioPrestador(1L, new Date(), new Date());

            assertNotNull(result);
        }
    }

    @Test
    void testGerarRelatorioCliente() throws SQLException, IOException {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(relatorioCliente.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        try (MockedStatic<JasperRunManager> mockedJasper = mockStatic(JasperRunManager.class)) {
            mockedJasper.when(
                    () -> JasperRunManager.runReportToPdf(any(InputStream.class), anyMap(), any(Connection.class)))
                    .thenReturn(new byte[10]);

            byte[] result = service.gerarRelatorioCliente(1L, new Date(), new Date());

            assertNotNull(result);
        }
    }

    @Test
    void testGerarRelatorioServicosPrestados() throws SQLException, IOException {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(relatorioServicosPrestados.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        try (MockedStatic<JasperRunManager> mockedJasper = mockStatic(JasperRunManager.class)) {
            mockedJasper.when(
                    () -> JasperRunManager.runReportToPdf(any(InputStream.class), anyMap(), any(Connection.class)))
                    .thenReturn(new byte[10]);

            byte[] result = service.gerarRelatorioServicosPrestados(new Date(), new Date());

            assertNotNull(result);
        }
    }

    @Test
    void testGerarRelatorioPrestador_Exception() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException("Error"));

        byte[] result = service.gerarRelatorioPrestador(1L, new Date(), new Date());

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testGerarRelatorioCliente_Exception() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException("Error"));

        byte[] result = service.gerarRelatorioCliente(1L, new Date(), new Date());

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testGerarRelatorioServicosPrestados_Exception() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException("Error"));

        byte[] result = service.gerarRelatorioServicosPrestados(new Date(), new Date());

        assertNotNull(result);
        assertEquals(0, result.length);
    }
}
