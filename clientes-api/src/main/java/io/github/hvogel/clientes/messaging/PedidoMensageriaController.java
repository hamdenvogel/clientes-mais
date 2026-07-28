package io.github.hvogel.clientes.messaging;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints do lab de mensageria (item 2).
 * Não substitui o CRUD {@code /api/pedidos}.
 */
@RestController
@RequestMapping("/api/pedidos/async")
public class PedidoMensageriaController {

	private final PedidoMessagePublisher publisher;
	private final PedidoMessageListener listener;

	public PedidoMensageriaController(PedidoMessagePublisher publisher, PedidoMessageListener listener) {
		this.publisher = publisher;
		this.listener = listener;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Map<String, Object> criarPedidoAsync(@Valid @RequestBody CriarPedidoAsyncRequest request) {
		PedidoMensagem mensagem = new PedidoMensagem(request.getDescricao(), request.getTotal(), false);
		publisher.publicar(mensagem);
		return respostaPublicacao(mensagem, "Pedido publicado na fila. Aguarde o consumidor.");
	}

	@PostMapping("/simular-falha")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Map<String, Object> simularFalha(@Valid @RequestBody CriarPedidoAsyncRequest request) {
		PedidoMensagem mensagem = new PedidoMensagem(request.getDescricao(), request.getTotal(), true);
		publisher.publicar(mensagem);
		return respostaPublicacao(mensagem,
				"Mensagem com forcarFalha=true publicada. Após retries, deve ir para a DLQ.");
	}

	@GetMapping("/status")
	public Map<String, Object> status() {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("processadosComSucesso", listener.getProcessados());
		body.put("enviadosParaDlq", listener.getEnviadosDlq());
		body.put("ultimosProcessados", listener.getUltimosProcessados());
		body.put("ultimosDlq", listener.getUltimosDlq());
		body.put("exchange", PedidoRabbitConfig.EXCHANGE);
		body.put("queue", PedidoRabbitConfig.QUEUE);
		body.put("dlq", PedidoRabbitConfig.DLQ);
		return body;
	}

	@GetMapping("/dlq")
	public List<PedidoMensagem> listarDlq() {
		return listener.getUltimosDlq();
	}

	private static Map<String, Object> respostaPublicacao(PedidoMensagem mensagem, String detalhe) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("messageId", mensagem.getMessageId());
		body.put("descricao", mensagem.getDescricao());
		body.put("total", mensagem.getTotal() != null ? mensagem.getTotal() : BigDecimal.ZERO);
		body.put("forcarFalha", mensagem.isForcarFalha());
		body.put("detalhe", detalhe);
		return body;
	}
}
