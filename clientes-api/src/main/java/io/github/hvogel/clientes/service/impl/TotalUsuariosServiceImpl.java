package io.github.hvogel.clientes.service.impl;

import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.model.repository.UsuarioRepository;
import io.github.hvogel.clientes.service.TotalUsuariosService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalUsuariosServiceImpl implements TotalUsuariosService {

	private final UsuarioRepository repository;

	@Override
	public long obterTotalUsuarios() {
		return repository.count();
	}

}
