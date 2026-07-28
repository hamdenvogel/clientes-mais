package io.github.hvogel.clientes.messaging;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Mensagem assíncrona de pedido (lab RabbitMQ — item 2 do roadmap).
 * Independente do CRUD JPA de {@code /api/pedidos}.
 */
public class PedidoMensagem {

	private String messageId;
	private String descricao;
	private BigDecimal total;
	private boolean forcarFalha;
	private Instant criadoEm;

	public PedidoMensagem() {
	}

	public PedidoMensagem(String descricao, BigDecimal total, boolean forcarFalha) {
		this.messageId = UUID.randomUUID().toString();
		this.descricao = descricao;
		this.total = total;
		this.forcarFalha = forcarFalha;
		this.criadoEm = Instant.now();
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public boolean isForcarFalha() {
		return forcarFalha;
	}

	public void setForcarFalha(boolean forcarFalha) {
		this.forcarFalha = forcarFalha;
	}

	public Instant getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(Instant criadoEm) {
		this.criadoEm = criadoEm;
	}

	@Override
	public String toString() {
		return "PedidoMensagem{messageId='%s', descricao='%s', total=%s, forcarFalha=%s, criadoEm=%s}"
				.formatted(messageId, descricao, total, forcarFalha, criadoEm);
	}
}
