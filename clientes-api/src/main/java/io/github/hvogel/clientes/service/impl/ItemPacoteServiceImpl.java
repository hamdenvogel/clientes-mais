package io.github.hvogel.clientes.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.hvogel.clientes.model.entity.ItemPacote;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.model.repository.ItemPacoteRepository;
import io.github.hvogel.clientes.service.ItemPacoteService;

@Service
public class ItemPacoteServiceImpl implements ItemPacoteService {
	
	private final ItemPacoteRepository repository;
		
	public ItemPacoteServiceImpl(ItemPacoteRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	@Transactional
	public ItemPacote salvar(ItemPacote itemPacote) {
		return repository.save(itemPacote);
	}

	@Override
	@Transactional
	public ItemPacote atualizar(ItemPacote itemPacote) {
		Objects.requireNonNull(itemPacote.getId());
		return repository.save(itemPacote);
	}

	@Override
	@Transactional
	public void deletar(ItemPacote itemPacote) {
		repository.delete(itemPacote);		
	}

	@Override
	public Optional<ItemPacote> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public Page<ItemPacote> obterPorId(Integer id, Pageable pageable) {
		return repository.findByIdItemPacote(id, pageable);
	}

	@Override
	public List<ItemPacote> obterTodos() {
		return repository.findAllByOrderByPacoteAsc();
	}

	@Override
	public Page<ItemPacote> recuperarTodos(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<ItemPacote> obterPorIdPacote(Integer idPacote, Pageable pageable) {
		return repository.findByIdPacote(idPacote, pageable);
	}

	@Override
	public Optional<ItemPacote> obterPorPacoteEServicoPrestado(Integer idPacote, Integer idServicoPrestado) {
		return repository.existsByPacoteAndServicoPrestado(idPacote, idServicoPrestado);
	}

	@Override
	@Transactional
	public void deletarPorPacoteEServicoPrestado(Integer idPacote, Integer idServicoPrestado) {
		repository.deletarPorPacoteEServicoPrestado(idPacote, idServicoPrestado);		
	}

	@Override
	public boolean existsByServicoPrestado(ServicoPrestado servicoPrestado) {
		return repository.existsByServicoPrestado(servicoPrestado);
	}
		
}
