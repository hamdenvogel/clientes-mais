package io.github.hvogel.clientes.service.impl;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.repository.ProdutosRepository;
import io.github.hvogel.clientes.service.TotalProdutosService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalProdutosServiceImpl implements TotalProdutosService {

	private final ProdutosRepository repository;

	@Override
	public long obterTotalProdutos() {
		return repository.count();
	}

}
