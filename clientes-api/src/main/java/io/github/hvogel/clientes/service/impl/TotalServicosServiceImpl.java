package io.github.hvogel.clientes.service.impl;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.repository.ServicoPrestadoRepository;
import io.github.hvogel.clientes.service.TotalServicosService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalServicosServiceImpl implements TotalServicosService {

	private final ServicoPrestadoRepository repository;

	@Override
	public long obterTotalServicos() {
		return repository.count();
	}
}
