package io.github.hvogel.clientes.service.impl;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.repository.PacoteRepository;
import io.github.hvogel.clientes.service.TotalPacotesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalPacotesServiceImpl implements TotalPacotesService {

	private final PacoteRepository repository;

	@Override
	public long obterTotalPacotes() {
		return repository.count();
	}

}
