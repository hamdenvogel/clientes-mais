package io.github.hvogel.clientes.rest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hvogel.clientes.model.grafico.GraficoAtendimentoLinear;
import io.github.hvogel.clientes.model.grafico.GraficoAtendimentoTorta;
import io.github.hvogel.clientes.model.grafico.GraficoStatusPacotePercentual;
import io.github.hvogel.clientes.model.grafico.GraficoTipoServico;
import io.github.hvogel.clientes.service.PacoteService;
import io.github.hvogel.clientes.service.ServicoPrestadoService;


@RestController
@RequestMapping("/api/grafico")
public class GraficoController {
	private final ServicoPrestadoService servicoService;
	private final PacoteService pacoteService;
		
	public GraficoController(ServicoPrestadoService servicoService, PacoteService pacoteService) {
		super();
		this.servicoService = servicoService;
		this.pacoteService = pacoteService;
	}

	@GetMapping("grafico-status-atendimento-por-periodo")
	public GraficoAtendimentoLinear  gerarGraficoStatusAtendimentoPorPeriodo() {
		
		List<String> monthsYear = servicoService.mesesGrafico();
		List<Integer> emAtendimento = servicoService.emAtendimento();
		List<Integer> finalizado = servicoService.finalizado();
		List<Integer> cancelado = servicoService.cancelado();
		
		return GraficoAtendimentoLinear.builder()
				.withMesAno(monthsYear)
			.withEmAtendimento(emAtendimento)
				.withCancelado(cancelado)
				.withFinalizado(finalizado)
				.build();
	}	
	
	@GetMapping("grafico-status-atendimento-quantidade")
	public GraficoAtendimentoTorta gerarGraficoStatusAtendimentoQuantidade() {
		
		List<String> statusDescriptions = servicoService.descricaoStatus();
		List<Integer> quantidades = servicoService.quantidadeServicos();
		
		return GraficoAtendimentoTorta.builder()
				.withStatusAtendimento(statusDescriptions)
				.withQuantidade(quantidades)
				.build();		
	}
	
	@GetMapping("grafico-status-pacote-percentual")
	public GraficoStatusPacotePercentual gerarGraficoStatusPacotePercentual() {
		
		List<BigDecimal> iPercentual = pacoteService.iPercentual();
		List<BigDecimal> aPercentual = pacoteService.aPercentual();
		List<BigDecimal> ePercentual = pacoteService.ePercentual();
		List<BigDecimal> cPercentual = pacoteService.cPercentual();
		List<BigDecimal> fPercentual = pacoteService.fPercentual();
		
		return GraficoStatusPacotePercentual.builder()
				.withIniciadoPercentual(iPercentual)
				.withAprovadoPercentual(aPercentual)
				.withExecutandoPercentual(ePercentual)
				.withCanceladoPercentual(cPercentual)
				.withFinalizadoPercentual(fPercentual)
				.build();		
	}
	
	@GetMapping("grafico-tipo-servico")
	public GraficoTipoServico  gerarGraficoTipoServicoPorPeriodo() {
		
		List<String> monthsYear = servicoService.mesesGraficoTipoServico();
		List<Integer> unitario = servicoService.tipoUnitario();
		List<Integer> pacote = servicoService.tipoPacote();		
		
		return GraficoTipoServico.builder()
				.withMesAno(monthsYear)
				.withUnitario(unitario)
				.withPacote(pacote)
				.build();
	}	
}
