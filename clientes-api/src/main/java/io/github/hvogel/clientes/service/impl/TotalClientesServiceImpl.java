package io.github.hvogel.clientes.service.impl;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.repository.ClienteRepository;
import io.github.hvogel.clientes.service.TotalClientesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalClientesServiceImpl implements TotalClientesService {

	private final ClienteRepository repository;

	@Override
	public long obterTotalClientes() {
		return repository.count();
	}

}
