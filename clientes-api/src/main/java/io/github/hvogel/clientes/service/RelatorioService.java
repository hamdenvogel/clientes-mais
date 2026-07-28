package io.github.hvogel.clientes.service;

import java.util.Date;

public interface RelatorioService {
	byte[] gerarRelatorioPrestador(Long idPrestador, Date dataInicio, Date dataFim);
	byte[] gerarRelatorioCliente(Long idCliente, Date dataInicio, Date dataFim);
	byte[] gerarRelatorioServicosPrestados(Date dataInicio, Date dataFim);
}
