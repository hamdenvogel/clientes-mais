package io.github.hvogel.clientes.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.model.entity.Pacote;
import io.github.hvogel.clientes.model.repository.PacoteRepository;
import io.github.hvogel.clientes.service.PacoteService;
import io.github.hvogel.clientes.service.ValidadorService;


@Service
public class PacoteServiceImpl implements PacoteService {
	
	private final PacoteRepository repository;
	private final ValidadorService validadorService;
	
	public PacoteServiceImpl(PacoteRepository repository, ValidadorService validadorService) {
		super();
		this.repository = repository;
		this.validadorService = validadorService;
	}

	@Override
	@Transactional
	public Pacote salvar(Pacote pacote) {
		validar(pacote);
		return repository.save(pacote);
	}

	@Override
	@Transactional
	public Pacote atualizar(Pacote pacote) {
		Objects.requireNonNull(pacote.getId());
		validarAtualizacao(pacote);
		return repository.save(pacote);
	}

	@Override
	public void deletar(Pacote pacote) {
		repository.delete(pacote);		
	}

	@Override
	public Optional<Pacote> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public Page<Pacote> obterPorId(Integer id, Pageable pageable) {
		return repository.findByIdPacote(id, pageable);
	}

	@Override
	public List<Pacote> obterTodos() {
		return repository.findAllByOrderByDescricaoAsc();
	}

	@Override
	public void validar(Pacote pacote) {
		String nomePacote = pacote.getDescricao().trim();
		Optional<Pacote> pacoteExistente = repository.findByDescricao(nomePacote);
		if (pacoteExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Descrição do pacote já cadastrada para outro pacote.");
		}		
		validadorService.validarTipoPacote(pacote.getStatus());
	}

	@Override
	public void validarAtualizacao(Pacote pacote) {
		String nomePacote = pacote.getDescricao().trim();
		Optional<Pacote> pacoteExistente = repository.findByDescricaoAndIdNot(nomePacote, pacote.getId());
		if (pacoteExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Descrição do pacote já cadastrada para outro pacote.");
		}
		validadorService.validarTipoPacote(pacote.getStatus());
	}

	@Override
	public Page<Pacote> recuperarTodos(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public List<BigDecimal> iPercentual() {
		return repository.iPercentual();
	}

	@Override
	public List<BigDecimal> aPercentual() {
		return repository.aPercentual();
	}

	@Override
	public List<BigDecimal> ePercentual() {
		return repository.ePercentual();
	}

	@Override
	public List<BigDecimal> cPercentual() {
		return repository.cPercentual();
	}

	@Override
	public List<BigDecimal> fPercentual() {
		return repository.fPercentual();
	}

	@Override
	public Page<Pacote> pesquisarPelaDescricao(String descricao, Pageable pageable) {
		return repository.findByDescricaoContainsAllIgnoreCase(descricao, pageable);
	}
  
}
