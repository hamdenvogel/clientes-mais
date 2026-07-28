package io.github.hvogel.clientes.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoMessagePublisher {

	private static final Logger log = LoggerFactory.getLogger(PedidoMessagePublisher.class);

	private final RabbitTemplate rabbitTemplate;

	public PedidoMessagePublisher(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public PedidoMensagem publicar(PedidoMensagem mensagem) {
		log.info("Publicando pedido na exchange {} rk={}: {}",
				PedidoRabbitConfig.EXCHANGE, PedidoRabbitConfig.ROUTING_KEY, mensagem);
		rabbitTemplate.convertAndSend(
				PedidoRabbitConfig.EXCHANGE,
				PedidoRabbitConfig.ROUTING_KEY,
				mensagem);
		return mensagem;
	}
}
