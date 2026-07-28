package io.github.hvogel.clientes.service.impl;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.repository.PrestadorRepository;
import io.github.hvogel.clientes.service.TotalPrestadoresService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalPrestadoresServiceImpl implements TotalPrestadoresService {

	private final PrestadorRepository repository;

	@Override
	public long obterTotalPrestadores() {
		return repository.count();
	}

}
