package io.github.hvogel.clientes.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class PedidoRabbitConfig {

	public static final String EXCHANGE = "clientes.pedidos.exchange";
	public static final String DLX = "clientes.pedidos.dlx";

	public static final String QUEUE = "clientes.pedidos.criado.queue";
	public static final String DLQ = "clientes.pedidos.criado.dlq";

	public static final String ROUTING_KEY = "pedidos.criado";
	public static final String DLQ_ROUTING_KEY = "pedidos.criado.dlq";

	@Bean
	public TopicExchange pedidosExchange() {
		return new TopicExchange(EXCHANGE, true, false);
	}

	@Bean
	public TopicExchange pedidosDlx() {
		return new TopicExchange(DLX, true, false);
	}

	@Bean
	public Queue pedidosQueue() {
		return QueueBuilder.durable(QUEUE)
				.withArgument("x-dead-letter-exchange", DLX)
				.withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
				.build();
	}

	@Bean
	public Queue pedidosDlq() {
		return QueueBuilder.durable(DLQ).build();
	}

	@Bean
	public Binding pedidosBinding(Queue pedidosQueue, TopicExchange pedidosExchange) {
		return BindingBuilder.bind(pedidosQueue).to(pedidosExchange).with(ROUTING_KEY);
	}

	@Bean
	public Binding pedidosDlqBinding(Queue pedidosDlq, TopicExchange pedidosDlx) {
		return BindingBuilder.bind(pedidosDlq).to(pedidosDlx).with(DLQ_ROUTING_KEY);
	}

	@Bean
	public MessageConverter jacksonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(jacksonMessageConverter);
		template.setExchange(EXCHANGE);
		template.setRoutingKey(ROUTING_KEY);
		return template;
	}

	@Bean
	public RetryOperationsInterceptor pedidosRetryInterceptor() {
		return RetryInterceptorBuilder.stateless()
				.maxAttempts(3)
				.backOffOptions(1000, 2.0, 5000)
				.recoverer(new RejectAndDontRequeueRecoverer())
				.build();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			ConnectionFactory connectionFactory,
			SimpleRabbitListenerContainerFactoryConfigurer configurer,
			MessageConverter jacksonMessageConverter,
			RetryOperationsInterceptor pedidosRetryInterceptor) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setMessageConverter(jacksonMessageConverter);
		factory.setDefaultRequeueRejected(false);
		factory.setAdviceChain(pedidosRetryInterceptor);
		return factory;
	}
}
