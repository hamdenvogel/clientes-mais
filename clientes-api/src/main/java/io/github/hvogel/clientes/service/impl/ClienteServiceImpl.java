package io.github.hvogel.clientes.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.enums.SearchOperation;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.model.repository.ClienteRepository;
import io.github.hvogel.clientes.model.specification.ClienteSpecification;
import io.github.hvogel.clientes.service.ClienteService;
import io.github.hvogel.clientes.service.GoogleService;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	private final ClienteRepository repository;
		private final ServicoPrestadoCascadeDeleteService servicoPrestadoCascadeDeleteService;
	private final GoogleService googleService;	

	public ClienteServiceImpl(ClienteRepository repository,
				ServicoPrestadoCascadeDeleteService servicoPrestadoCascadeDeleteService,
				GoogleService googleService) {
		super();
		this.repository = repository;
			this.servicoPrestadoCascadeDeleteService = servicoPrestadoCascadeDeleteService;
		this.googleService = googleService;
	}

	@Override
	@Transactional
	public Cliente salvar(Cliente cliente) {
		validar(cliente);
		return repository.save(cliente);
	}

	@Override
	@Transactional
	public Cliente atualizar(Cliente cliente) {		
		Objects.requireNonNull(cliente.getId());
		validarAtualizacao(cliente);
		return repository.save(cliente);
	}

	@Override
	@Transactional
	public void deletar(Cliente cliente) {
		servicoPrestadoCascadeDeleteService.deleteByClienteId(cliente.getId());
		repository.delete(cliente);	
	}

	@Override
	public Optional<Cliente> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Cliente> obterTodos() {
		return repository.findAllByOrderByNomeAsc();
	}

	@Override
	public void validar(Cliente cliente) {
		String cpfDesformatado = cliente.getCpf().replace(".", "").replace("-", "").trim();
		cliente.setCpf(cpfDesformatado);
		
		String nomeComAInicialMaiuscula = cliente.getNome();
		nomeComAInicialMaiuscula = 
				nomeComAInicialMaiuscula.substring(0,1).toUpperCase() + 
				nomeComAInicialMaiuscula.substring(1).toLowerCase();
		
		cliente.setNome(nomeComAInicialMaiuscula);
		
		Optional<Cliente> nomeExistente = repository.findByNome(cliente.getNome().trim());
		if (nomeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Nome do cliente já cadastrado para outro cliente.");
		}
		
		Optional<Cliente> cpfExistente = repository.findByCpf(cliente.getCpf());
		if (cpfExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"CPF já cadastrado para outro cliente.");
		}				
		
		if (cliente.getCpf().length() > 11) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"CPF com tamanho superior a 11 caracteres.");
		}	
		
		if (cliente.getCep() != null && !cliente.getCep().trim().equals("")){
			String cepDesformatado = cliente.getCep().replace(".", "").replace("-", "").trim();
			cliente.setCep(cepDesformatado);
		}
		
		googleService.validarCaptchaPreenchido(cliente.getCaptcha());
	}

	@Override
	public void validarAtualizacao(Cliente cliente) {
		String cpfDesformatado = cliente.getCpf().replace(".", "").replace("-", "").trim();
		cliente.setCpf(cpfDesformatado);
		
		Optional<Cliente> clienteExistente = repository.findByCpfAndIdNot(cpfDesformatado, 
				cliente.getId());		
		
		if (clienteExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"CPF já cadastrado para outro cliente.");					
		}
		
		String nomeComAInicialMaiuscula = cliente.getNome();
		nomeComAInicialMaiuscula = 
				nomeComAInicialMaiuscula.substring(0,1).toUpperCase() + 
				nomeComAInicialMaiuscula.substring(1).toLowerCase();
		
		cliente.setNome(nomeComAInicialMaiuscula);
		
		String nomeCliente = cliente.getNome().trim();
		Optional<Cliente> nomeExistente = repository.findByNomeAndIdNot(nomeCliente, 
				cliente.getId());		
		
		if (nomeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Nome do cliente já cadastrado para outro cliente.");					
		}
				
		if (cliente.getCpf().length() > 11) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"CPF com tamanho superior a 11 caracteres.");
		}
		
		if (cliente.getCep() != null && !cliente.getCep().trim().equals("")){
			String cepDesformatado = cliente.getCep().replace(".", "").replace("-", "").trim();
			cliente.setCep(cepDesformatado);
		}
		
		googleService.validarCaptchaPreenchido(cliente.getCaptcha());		
	}

	@Override
	public Page<Cliente> recuperarTodos(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<Cliente> pesquisarPeloNome(String nome, Pageable pageable) {
		ClienteSpecification clienteSpecification = new ClienteSpecification();
		clienteSpecification.add(new SearchCriteria("nome", nome, SearchOperation.LIKE));
		return repository.findAll(clienteSpecification, pageable);
	}

}
