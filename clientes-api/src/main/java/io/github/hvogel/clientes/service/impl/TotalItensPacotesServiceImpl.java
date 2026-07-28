package io.github.hvogel.clientes.service.impl;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.repository.ItemPacoteRepository;
import io.github.hvogel.clientes.service.TotalItensPacotesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalItensPacotesServiceImpl implements TotalItensPacotesService {

	private final ItemPacoteRepository repository;

	@Override
	public long obterTotalItensPacotes(Integer idPacote) {
		return repository.countByPacoteId(idPacote);
	}

}
