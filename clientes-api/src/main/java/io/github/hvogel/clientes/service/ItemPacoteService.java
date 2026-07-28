package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.model.entity.ItemPacote;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;

public interface ItemPacoteService {
	ItemPacote salvar(ItemPacote itemPacote);
	ItemPacote atualizar(ItemPacote itemPacote);
	void deletar(ItemPacote itemPacote);
	Optional<ItemPacote> obterPorId(Integer id);
	Page<ItemPacote> obterPorId(Integer id, Pageable pageable);
	List<ItemPacote> obterTodos();
	Page<ItemPacote> recuperarTodos(Pageable pageable);
	Page<ItemPacote> obterPorIdPacote(Integer idPacote, Pageable pageable);
	Optional<ItemPacote> obterPorPacoteEServicoPrestado(Integer idPacote, Integer idServicoPrestado);
	void deletarPorPacoteEServicoPrestado(Integer idPacote, Integer idServicoPrestado);	
	boolean existsByServicoPrestado(ServicoPrestado servicoPrestado);
}
