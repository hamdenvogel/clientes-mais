package io.github.hvogel.clientes.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractController<E extends IBaseEntity, D extends Serializable, S extends CommonService<E, D>> {

	protected final S service;

	protected AbstractController(S service) {
		this.service = service;
	}

	@GetMapping
	public Page<D> findAll(Pageable pageable) {
		Page<E> result = service.findAll(pageable);
		return new PageImpl<>(result.stream()
				.map(service::convertToDto)
				.toList(), pageable,
				result.getTotalElements());
	}

	public CommonService<E, D> service() {
		return service;
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<D> findOne(@PathVariable("id") Long id) {
		Optional<E> entity = service.findOneById(id);
		return entity.map(e -> new ResponseEntity<>(service.convertToDto(e), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		service.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping
	@Transactional
	public ResponseEntity<D> insert(@Valid @RequestBody D dto) {
		E entity = service.convertToEntity(dto);
		return new ResponseEntity<>(service.convertToDto(service.saveAndFlush(entity)),
				HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<D> update(@PathVariable("id") Long id, @Valid @RequestBody D dto) {
		Optional<E> obj = service.findOneById(id);
		if (obj.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		E entity = service.convertToEntity(dto);
		entity.setId(id);
		service.save(entity);
		return ResponseEntity.noContent().build();
	}
}
