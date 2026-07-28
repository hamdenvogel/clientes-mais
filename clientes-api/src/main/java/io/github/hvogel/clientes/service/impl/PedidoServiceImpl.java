package io.github.hvogel.clientes.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.hvogel.clientes.enums.StatusPedido;
import io.github.hvogel.clientes.exception.PedidoNaoEncontradoException;
import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.ItemPedido;
import io.github.hvogel.clientes.model.entity.Pedido;
import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.model.repository.ItemsPedidoRepository;
import io.github.hvogel.clientes.model.repository.PedidosRepository;
import io.github.hvogel.clientes.model.repository.ProdutosRepository;
import io.github.hvogel.clientes.model.repository.ServicoPrestadoRepository;
import io.github.hvogel.clientes.rest.dto.ItemPedidoDTO;
import io.github.hvogel.clientes.rest.dto.PedidoDTO;
import io.github.hvogel.clientes.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidosRepository repository;
    private final ServicoPrestadoRepository servicoPrestadoRepository;
    private final ProdutosRepository produtosRepository;
    private final ItemsPedidoRepository itemsPedidoRepository;

    public PedidoServiceImpl(PedidosRepository repository, ServicoPrestadoRepository servicoPrestadoRepository,
            ProdutosRepository produtosRepository, ItemsPedidoRepository itemsPedidoRepository) {
        super();
        this.repository = repository;
        this.servicoPrestadoRepository = servicoPrestadoRepository;
        this.produtosRepository = produtosRepository;
        this.itemsPedidoRepository = itemsPedidoRepository;
    }

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idServico = dto.getServico();
        ServicoPrestado servicoPrestado = servicoPrestadoRepository
                .findById(idServico)
                .orElseThrow(() -> new RegraNegocioException("Código do Serviço inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setServicoPrestado(servicoPrestado);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(PedidoNaoEncontradoException::new);
        pedido.setStatus(statusPedido);
        repository.save(pedido);
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
        if (items.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: " + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).toList();

    }

    @Override
    public List<Pedido> findByIdFetchItensAll() {
        return repository.findByIdFetchItensAll();
    }

    @Override
    public List<Pedido> findByIdFetchItensId(Integer id) {
        return repository.findByIdFetchItensId(id);
    }

}
