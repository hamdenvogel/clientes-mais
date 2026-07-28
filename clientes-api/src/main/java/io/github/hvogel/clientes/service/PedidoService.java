package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import io.github.hvogel.clientes.enums.StatusPedido;
import io.github.hvogel.clientes.model.entity.Pedido;
import io.github.hvogel.clientes.rest.dto.PedidoDTO;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);

    List<Pedido> findByIdFetchItensAll();

    List<Pedido> findByIdFetchItensId(Integer id);
}
