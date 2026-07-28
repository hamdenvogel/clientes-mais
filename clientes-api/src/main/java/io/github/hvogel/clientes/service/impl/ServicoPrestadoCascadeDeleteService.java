package io.github.hvogel.clientes.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.hvogel.clientes.model.repository.ChamadoRepository;
import io.github.hvogel.clientes.model.repository.DiagnosticoRepository;
import io.github.hvogel.clientes.model.repository.EquipamentoRepository;
import io.github.hvogel.clientes.model.repository.ItemPacoteRepository;
import io.github.hvogel.clientes.model.repository.ItemsPedidoRepository;
import io.github.hvogel.clientes.model.repository.PedidosRepository;
import io.github.hvogel.clientes.model.repository.ServicoPrestadoRepository;
import io.github.hvogel.clientes.model.repository.SolucaoRepository;

@Service
public class ServicoPrestadoCascadeDeleteService {

	private final ChamadoRepository chamadoRepository;
	private final ItemPacoteRepository itemPacoteRepository;
	private final ItemsPedidoRepository itemsPedidoRepository;
	private final PedidosRepository pedidosRepository;
	private final DiagnosticoRepository diagnosticoRepository;
	private final EquipamentoRepository equipamentoRepository;
	private final SolucaoRepository solucaoRepository;
	private final ServicoPrestadoRepository servicoPrestadoRepository;

	public ServicoPrestadoCascadeDeleteService(ChamadoRepository chamadoRepository,
			ItemPacoteRepository itemPacoteRepository,
			ItemsPedidoRepository itemsPedidoRepository,
			PedidosRepository pedidosRepository,
			DiagnosticoRepository diagnosticoRepository,
			EquipamentoRepository equipamentoRepository,
			SolucaoRepository solucaoRepository,
			ServicoPrestadoRepository servicoPrestadoRepository) {
		this.chamadoRepository = chamadoRepository;
		this.itemPacoteRepository = itemPacoteRepository;
		this.itemsPedidoRepository = itemsPedidoRepository;
		this.pedidosRepository = pedidosRepository;
		this.diagnosticoRepository = diagnosticoRepository;
		this.equipamentoRepository = equipamentoRepository;
		this.solucaoRepository = solucaoRepository;
		this.servicoPrestadoRepository = servicoPrestadoRepository;
	}

	@Transactional
	public void deleteByClienteId(Integer clienteId) {
		chamadoRepository.deleteByClienteId(clienteId);
		itemPacoteRepository.deleteByServicoPrestadoClienteId(clienteId);
		itemsPedidoRepository.deleteByPedidoServicoPrestadoClienteId(clienteId);
		pedidosRepository.deleteByServicoPrestadoClienteId(clienteId);
		diagnosticoRepository.deleteByServicoPrestadoClienteId(clienteId);
		equipamentoRepository.deleteByServicoPrestadoClienteId(clienteId);
		solucaoRepository.deleteByServicoPrestadoClienteId(clienteId);
		servicoPrestadoRepository.deleteByIdCliente(clienteId);
	}

	@Transactional
	public void deleteByPrestadorId(Integer prestadorId) {
		itemPacoteRepository.deleteByServicoPrestadoPrestadorId(prestadorId);
		itemsPedidoRepository.deleteByPedidoServicoPrestadoPrestadorId(prestadorId);
		pedidosRepository.deleteByServicoPrestadoPrestadorId(prestadorId);
		diagnosticoRepository.deleteByServicoPrestadoPrestadorId(prestadorId);
		equipamentoRepository.deleteByServicoPrestadoPrestadorId(prestadorId);
		solucaoRepository.deleteByServicoPrestadoPrestadorId(prestadorId);
		servicoPrestadoRepository.deleteByPrestadorId(prestadorId);
	}
}
