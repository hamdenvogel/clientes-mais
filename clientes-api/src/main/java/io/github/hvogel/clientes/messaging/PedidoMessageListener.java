package io.github.hvogel.clientes.messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoMessageListener {

	private static final Logger log = LoggerFactory.getLogger(PedidoMessageListener.class);

	private final AtomicInteger processados = new AtomicInteger();
	private final AtomicInteger enviadosDlq = new AtomicInteger();
	private final List<PedidoMensagem> ultimosProcessados = new CopyOnWriteArrayList<>();
	private final List<PedidoMensagem> ultimosDlq = new CopyOnWriteArrayList<>();

	@RabbitListener(queues = PedidoRabbitConfig.QUEUE)
	public void consumirPedidoCriado(PedidoMensagem mensagem) {
		log.info("Consumindo pedido messageId={}", mensagem.getMessageId());
		if (mensagem.isForcarFalha()) {
			log.warn("Simulando falha de processamento messageId={} (retry → DLQ)", mensagem.getMessageId());
			throw new PedidoProcessamentoException(
					"Falha simulada no processamento do pedido " + mensagem.getMessageId());
		}
		processados.incrementAndGet();
		lembrar(ultimosProcessados, mensagem);
		log.info("Pedido processado com sucesso messageId={} total={}",
				mensagem.getMessageId(), mensagem.getTotal());
	}

	@RabbitListener(queues = PedidoRabbitConfig.DLQ)
	public void consumirDlq(PedidoMensagem mensagem) {
		enviadosDlq.incrementAndGet();
		lembrar(ultimosDlq, mensagem);
		log.error("Pedido chegou na DLQ messageId={} descricao={}",
				mensagem.getMessageId(), mensagem.getDescricao());
	}

	public int getProcessados() {
		return processados.get();
	}

	public int getEnviadosDlq() {
		return enviadosDlq.get();
	}

	public List<PedidoMensagem> getUltimosProcessados() {
		return Collections.unmodifiableList(new ArrayList<>(ultimosProcessados));
	}

	public List<PedidoMensagem> getUltimosDlq() {
		return Collections.unmodifiableList(new ArrayList<>(ultimosDlq));
	}

	private static void lembrar(List<PedidoMensagem> lista, PedidoMensagem mensagem) {
		lista.add(0, mensagem);
		while (lista.size() > 20) {
			lista.remove(lista.size() - 1);
		}
	}
}
