package io.github.hvogel.clientes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.rest.dto.ServicoPrestadoProjectionDTO;

public interface ServicoPrestadoService {
	ServicoPrestado salvar(ServicoPrestado servicoPrestado);
	ServicoPrestado atualizar(ServicoPrestado servicoPrestado);
	void deletar(ServicoPrestado servicoPrestado);
	Optional<ServicoPrestado> obterPorId(Integer id);
	List<ServicoPrestado> pesquisarNomeClienteEMes(String nome, Integer mes);
	Optional<String> existeDescricaoNoMes(String descricao, Integer mes);
	void validaExistenciaDeDescricaoNoMes(String descricao, Integer mes);
	void isDataValida(String data);
	void isValorValido(String valor);
	List<ServicoPrestado> pesquisarParcialPorDescricao(String descricao);
	void validarValoresIniciais(String data, String preco, String descricao, Integer mes, 
			String status, String captcha, String tipo);
	void isStatusValido(String status);
	Page<ServicoPrestado> pesquisaAvancada(ServicoPrestado servicoPrestado, Pageable pageable);
	List<ServicoPrestado> pesquisarPorCliente(Cliente cliente);
	Page<ServicoPrestado> recuperarTodos(Pageable pageable);
	Page<ServicoPrestado> pesquisarPorDescricao(String descricao, Pageable pageable);
	Page<ServicoPrestado> pesquisarPorIdCliente(Integer id, Pageable pageable);
	void validarValoresIniciaisAlteracao(String data, String preco, String status, String captcha, String tipo);
	void deletarPorCliente(Integer idCliente);
	List<ServicoPrestado> pesquisarPorIdCliente(Integer idCliente);
	boolean existsByCliente(Cliente cliente);
	List<String> mesesGrafico();
	List<Integer> emAtendimento();
	List<Integer> finalizado();
	List<Integer> cancelado();	
	List<String> descricaoStatus();
	List<Integer> quantidadeServicos();
	void isTipoValido(String tipo);
	List<String> mesesGraficoTipoServico();
	List<Integer> tipoUnitario();
	List<Integer> tipoPacote();
	Page<ServicoPrestado> pesquisarPeloNomeDoCliente(String nome, Pageable pageable);
	Page<ServicoPrestado> obterServicosAindaNaoVinculados(Pageable pageable);
	List<ServicoPrestadoProjectionDTO> findAllServicoPrestadoProjectionDTO();
}
