package io.github.hvogel.clientes.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.entity.ServicoPrestado;
import io.github.hvogel.clientes.model.repository.ServicoPrestadoRepository;
import io.github.hvogel.clientes.rest.dto.ServicoPrestadoProjectionDTO;
import io.github.hvogel.clientes.service.GoogleService;
import io.github.hvogel.clientes.service.ServicoPrestadoService;
import io.github.hvogel.clientes.service.ValidadorService;


@Service
public class ServicoPrestadoServiceImpl implements ServicoPrestadoService {
	
	private final ValidadorService validadorService;
	private final GoogleService googleService;
	private final ServicoPrestadoRepository repository;
		
	public ServicoPrestadoServiceImpl(ValidadorService validadorService, GoogleService googleService,
			ServicoPrestadoRepository repository) {
		super();
		this.validadorService = validadorService;
		this.googleService = googleService;
		this.repository = repository;
	}

	@Override
	@Transactional
	public ServicoPrestado salvar(ServicoPrestado servicoPrestado) {
		return repository.save(servicoPrestado);
	}

	@Override
	public List<ServicoPrestado> pesquisarNomeClienteEMes(String nome, Integer mes) {
		return repository.findByNomeClienteAndMes(nome, mes);
	}

	@Override
	public void isDataValida(String data) {
		validadorService.validarData(data);		
	}

	@Override
	public void isValorValido(String valor) {
		validadorService.validarValorNumerico(valor);		
	}

	@Override
	public Optional<String> existeDescricaoNoMes(String descricao, Integer mes) {
		return repository.existsByDescricaoAndMes(descricao, mes);
	}

	@Override
	public void validaExistenciaDeDescricaoNoMes(String descricao, Integer mes) {
		Optional<String> descricaoExiste = this.existeDescricaoNoMes(descricao, mes);
		
		if (descricaoExiste.isPresent()) {
			throw new RegraNegocioException("Já existe essa mesma descrição para o mesmo mês.");
		}		
	}

	@Override
	@Transactional
	public ServicoPrestado atualizar(ServicoPrestado servicoPrestado) {
		Objects.requireNonNull(servicoPrestado.getId());
		return repository.save(servicoPrestado);
	}

	@Override
	@Transactional
	public void deletar(ServicoPrestado servicoPrestado) {
		repository.delete(servicoPrestado);		
	}

	@Override
	public Optional<ServicoPrestado> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<ServicoPrestado> pesquisarParcialPorDescricao(String descricao) {
		List<ServicoPrestado> listaServicos = repository.findByDescricaoContainsAllIgnoreCase(descricao);
		
		if (listaServicos.isEmpty()) {
			throw new RegraNegocioException("Pesquisa não encontrada.");
		}
		return listaServicos;
	}

	@Override
	public void validarValoresIniciais(String data, String preco, String descricao,
			Integer mes, String status, String captcha, String tipo) {
		this.isDataValida(data);
		this.isValorValido(preco);
		this.validaExistenciaDeDescricaoNoMes(descricao, mes);		
		this.isStatusValido(status);
		this.isTipoValido(tipo);
		googleService.validarCaptchaPreenchido(captcha);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ServicoPrestado> pesquisaAvancada(ServicoPrestado servicoPrestado, Pageable pageable) {
		Example<ServicoPrestado> example = Example.of(servicoPrestado,
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example, pageable);
	}

	@Override
	public void isStatusValido(String status) {
		validadorService.validarStatus(status);		
	}

	@Override
	public List<ServicoPrestado> pesquisarPorCliente(Cliente cliente) {
		return repository.findByCliente(cliente);
	}

	@Override
	public Page<ServicoPrestado> recuperarTodos(Pageable pageable) {		
		return repository.findAll(pageable);
	}

	@Override
	public Page<ServicoPrestado> pesquisarPorDescricao(String descricao, Pageable pageable) {
		return repository.findByDescricaoContainsAllIgnoreCase(descricao, pageable);
	}

	@Override
	public Page<ServicoPrestado> pesquisarPorIdCliente(Integer id, Pageable pageable) {
		return repository.findByIdCliente(id, pageable);
	}

	@Override
	public void validarValoresIniciaisAlteracao(String data, String preco, String status, String captcha, String tipo) {
		this.isDataValida(data);
		this.isValorValido(preco);
		this.isStatusValido(status);
		this.isTipoValido(tipo);
		googleService.validarCaptchaPreenchido(captcha);
	}

	@Override
	@Transactional
	public void deletarPorCliente(Integer idCliente) {
		repository.deleteByIdCliente(idCliente);
	}

	@Override
	public List<ServicoPrestado> pesquisarPorIdCliente(Integer idCliente) {
		List<ServicoPrestado> listaServicosPrestados = repository.findByIdCliente(idCliente);
			if (listaServicosPrestados.isEmpty()) {
				throw new RegraNegocioException("Não existe serviços cadastrados para este cliente.");
		} 
		
		return listaServicosPrestados;	
	}

	@Override
	public boolean existsByCliente(Cliente cliente) {
		return repository.existsByCliente(cliente);
	}

	@Override
	public List<String> mesesGrafico() {
		return repository.mesesGrafico();
	}

	@Override
	public List<Integer> emAtendimento() {
		return repository.emAtendimento();
	}

	@Override
	public List<Integer> finalizado() {
		return repository.finalizado();
	}

	@Override
	public List<Integer> cancelado() {
		return repository.cancelado();
	}

	@Override
	public List<String> descricaoStatus() {
		return repository.descricaoStatus();
	}

	@Override
	public List<Integer> quantidadeServicos() {
		return repository.quantidadeServicos();
	}

	@Override
	public void isTipoValido(String tipo) {
		validadorService.validarTipoServico(tipo);		
	}

	@Override
	public List<String> mesesGraficoTipoServico() {
		return repository.mesesGraficoTipoServico();
	}

	@Override
	public List<Integer> tipoUnitario() {
		return repository.tipoUnitario();
	}

	@Override
	public List<Integer> tipoPacote() {
		return repository.tipoPacote();
	}

	@Override
	public Page<ServicoPrestado> pesquisarPeloNomeDoCliente(String nome, Pageable pageable) {
		return repository.findByNomeCliente(nome, pageable);
	}

	@Override
	public Page<ServicoPrestado> obterServicosAindaNaoVinculados(Pageable pageable) {
		return repository.obterServicosAindaNaoVinculados(pageable);
	}

	@Override
	public List<ServicoPrestadoProjectionDTO> findAllServicoPrestadoProjectionDTO() {
		return repository.findAllServicoPrestadoProjectionDTO();
	}

}
