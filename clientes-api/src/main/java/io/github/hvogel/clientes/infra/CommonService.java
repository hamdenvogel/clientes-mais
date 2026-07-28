package io.github.hvogel.clientes.infra;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommonService<E extends IBaseEntity, D extends Serializable> {
	E save(E entity);

	void delete(E entity);

	void deleteById(Long id);

	List<E> findAll();

	Optional<E> findOneById(Long id);

	Page<E> findAll(Pageable pageable);

	E saveAndFlush(E entity);

	Page<E> findByExample(Example<E> ex, Pageable pageable);

	Optional<E> findOneByExample(Example<E> ex);

	D convertToDto(E entity);

	E convertToEntity(D dto);

	default void onBeforeSave(E enttity) {
	}

	default void onAfterSave(E entity) {
	}

}
