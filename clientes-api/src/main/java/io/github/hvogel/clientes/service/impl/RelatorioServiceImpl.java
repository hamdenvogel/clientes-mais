package io.github.hvogel.clientes.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hvogel.clientes.util.Messages;

@Service
@RequiredArgsConstructor
public class RelatorioServiceImpl implements RelatorioService {

	private static final Logger log = LoggerFactory.getLogger(RelatorioServiceImpl.class);

	@Value("classpath:reports/relatorio-prestador.jasper")
	private Resource relatorioPrestador;

	@Value("classpath:reports/relatorio-clientes2.jasper")
	private Resource relatorioCliente;

	@Value("classpath:reports/relatorio-servicos-prestados.jasper")
	private Resource relatorioServicosPrestados;

	private final DataSource dataSource;

	private static final String DATA_INICIO = Messages.PARAM_DATA_INICIO;
	private static final String DATA_FIM = Messages.PARAM_DATA_FIM;

	@Override
	public byte[] gerarRelatorioPrestador(Long idPrestador, Date dataInicio, Date dataFim) {

		try (
				Connection connection = dataSource.getConnection();) {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("ID_PRESTADOR", idPrestador);
			parametros.put(DATA_INICIO, dataInicio);
			parametros.put(DATA_FIM, dataFim);
			return JasperRunManager.runReportToPdf(
					relatorioPrestador.getInputStream(),
					parametros,
					connection);

		} catch (SQLException | JRException | IOException e) {
			log.error("Erro ao gerar relatorio de prestador", e);
		}

		return new byte[0];
	}

	@Override
	public byte[] gerarRelatorioCliente(Long idCliente, Date dataInicio, Date dataFim) {

		try (
				Connection connection = dataSource.getConnection();) {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("ID_CLIENTE", idCliente);
			parametros.put(DATA_INICIO, dataInicio);
			parametros.put(DATA_FIM, dataFim);
			return JasperRunManager.runReportToPdf(
					relatorioCliente.getInputStream(),
					parametros,
					connection);

		} catch (SQLException | JRException | IOException e) {
			log.error("Erro ao gerar relatorio de cliente", e);
		}

		return new byte[0];
	}

	@Override
	public byte[] gerarRelatorioServicosPrestados(Date dataInicio, Date dataFim) {

		try (
				Connection connection = dataSource.getConnection();) {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put(DATA_INICIO, dataInicio);
			parametros.put(DATA_FIM, dataFim);
			return JasperRunManager.runReportToPdf(
					relatorioServicosPrestados.getInputStream(),
					parametros,
					connection);

		} catch (SQLException | JRException | IOException e) {
			log.error("Erro ao gerar relatorio de servicos prestados", e);
		}

		return new byte[0];
	}

}
