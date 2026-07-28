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
import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.model.repository.PrestadorRepository;
import io.github.hvogel.clientes.model.specification.PrestadorSpecification;
import io.github.hvogel.clientes.service.GoogleService;
import io.github.hvogel.clientes.service.PrestadorService;

import io.github.hvogel.clientes.util.Messages;

@Service
public class PrestadorServiceImpl implements PrestadorService {

	private static final String CAMPO_AVALIACAO_OBRIGATORIO = Messages.CAMPO_AVALIACAO_OBRIGATORIO;
	private final PrestadorRepository repository;
	private final ServicoPrestadoCascadeDeleteService servicoPrestadoCascadeDeleteService;
	private final GoogleService googleService;

	public PrestadorServiceImpl(PrestadorRepository repository,
			ServicoPrestadoCascadeDeleteService servicoPrestadoCascadeDeleteService,
			GoogleService googleService) {
		super();
		this.repository = repository;
		this.servicoPrestadoCascadeDeleteService = servicoPrestadoCascadeDeleteService;
		this.googleService = googleService;
	}

	@Override
	@Transactional
	public Prestador salvar(Prestador prestador) {
		validar(prestador);
		return repository.save(prestador);
	}

	@Override
	@Transactional
	public Prestador atualizar(Prestador prestador) {
		Objects.requireNonNull(prestador.getId());
		validarAtualizacao(prestador);
		return repository.save(prestador);
	}

	@Override
	@Transactional
	public void deletar(Prestador prestador) {
		servicoPrestadoCascadeDeleteService.deleteByPrestadorId(prestador.getId());
		repository.delete(prestador);
	}

	@Override
	public Optional<Prestador> obterPorId(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<Prestador> obterTodos() {
		return repository.findAllByOrderByNomeAsc();
	}

	@Override
	public void validar(Prestador prestador) {
		String cpfDesformatado = prestador.getCpf().replace(".", "").replace("-", "").trim();
		prestador.setCpf(cpfDesformatado);

		String nomeComAInicialMaiuscula = prestador.getNome();
		nomeComAInicialMaiuscula = nomeComAInicialMaiuscula.substring(0, 1).toUpperCase() +
				nomeComAInicialMaiuscula.substring(1).toLowerCase();

		prestador.setNome(nomeComAInicialMaiuscula);

		Optional<Prestador> nomeExistente = repository.findByNome(prestador.getNome().trim());
		if (nomeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Nome do prestador já cadastrado para outro prestador.");
		}

		Optional<Prestador> cpfExistente = repository.findByCpf(prestador.getCpf());
		if (cpfExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"CPF já cadastrado para outro prestador.");
		}

		if (prestador.getCpf().length() > 11) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"CPF com tamanho superior a 11 caracteres.");
		}

		if (prestador.getAvaliacao() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CAMPO_AVALIACAO_OBRIGATORIO);
		}

		if (prestador.getAvaliacao() == 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CAMPO_AVALIACAO_OBRIGATORIO);
		}

		googleService.validarCaptchaPreenchido(prestador.getCaptcha());
	}

	@Override
	public void validarAtualizacao(Prestador prestador) {
		String cpfDesformatado = prestador.getCpf().replace(".", "").replace("-", "").trim();
		prestador.setCpf(cpfDesformatado);

		Optional<Prestador> prestadorExistente = repository.findByCpfAndIdNot(cpfDesformatado,
				prestador.getId());

		if (prestadorExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"CPF já cadastrado para outro prestador.");
		}

		String nomeComAInicialMaiuscula = prestador.getNome();
		nomeComAInicialMaiuscula = nomeComAInicialMaiuscula.substring(0, 1).toUpperCase() +
				nomeComAInicialMaiuscula.substring(1).toLowerCase();

		prestador.setNome(nomeComAInicialMaiuscula);

		String nomePrestador = prestador.getNome().trim();
		Optional<Prestador> nomeExistente = repository.findByNomeAndIdNot(nomePrestador,
				prestador.getId());

		if (nomeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Nome do prestador já cadastrado para outro prestador.");
		}

		if (prestador.getCpf().length() > 11) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"CPF com tamanho superior a 11 caracteres.");
		}

		if (prestador.getAvaliacao() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CAMPO_AVALIACAO_OBRIGATORIO);
		}

		if (prestador.getAvaliacao() == 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CAMPO_AVALIACAO_OBRIGATORIO);
		}

		googleService.validarCaptchaPreenchido(prestador.getCaptcha());
	}

	@Override
	public Page<Prestador> recuperarTodos(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<Prestador> obterPorId(Integer id, Pageable pageable) {
		return repository.findByIdPrestador(id, pageable);
	}

	@Override
	public Page<Prestador> pesquisarPeloNome(String nome, Pageable pageable) {
		PrestadorSpecification prestadorSpecification = new PrestadorSpecification();
		prestadorSpecification.add(new SearchCriteria("nome", nome, SearchOperation.LIKE));
		return repository.findAll(prestadorSpecification, pageable);
	}

	@Override
	public Page<Prestador> executaCriteria(List<SearchCriteria> searchCriteria, Pageable pageable) {
		PrestadorSpecification prestadorSpecification = new PrestadorSpecification();
		searchCriteria.stream().map(searchCriteriaValue -> new SearchCriteria(searchCriteriaValue.getKey(),
				searchCriteriaValue.getValue(), searchCriteriaValue.getOperation()))
				.forEach(prestadorSpecification::add);
		return repository.findAll(prestadorSpecification, pageable);
	}

}
