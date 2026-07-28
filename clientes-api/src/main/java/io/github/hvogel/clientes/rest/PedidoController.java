package io.github.hvogel.clientes.rest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.enums.StatusPedido;
import io.github.hvogel.clientes.model.entity.ItemPedido;
import io.github.hvogel.clientes.model.entity.Pedido;
import io.github.hvogel.clientes.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.hvogel.clientes.rest.dto.InformacaoItemPedidoDTO;
import io.github.hvogel.clientes.rest.dto.InformacoesPedidoDTO;
import io.github.hvogel.clientes.rest.dto.PedidoDTO;
import io.github.hvogel.clientes.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id) {
        return service
                .obterPedidoCompleto(id)
                .map(this::converter)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    @GetMapping("todos")
    public List<Pedido> findByIdFetchItensAll() {
        return service.findByIdFetchItensAll();
    }

    @GetMapping("todos/{id}")
    public List<Pedido> findByIdFetchItensId(@PathVariable Integer id) {
        return service.findByIdFetchItensId(id);
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id,
            @RequestBody AtualizacaoStatusPedidoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO
                .builder()
                .withCodigo(pedido.getId())
                .withDataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .withTotal(pedido.getTotal())
                .withStatus(pedido.getStatus().name())
                .withItems(converter(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> InformacaoItemPedidoDTO
                        .builder().withDescricaoProduto(item.getProduto().getDescricao())
                        .withPrecoUnitario(item.getProduto().getPreco())
                        .withQuantidade(item.getQuantidade())
                        .build())
                .toList();
    }

}
