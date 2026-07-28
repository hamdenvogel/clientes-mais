package io.github.hvogel.clientes.infra;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractService<E extends IBaseEntity, D extends Serializable, R extends CommonRepository<E>>
        implements
        CommonService<E, D> {

    protected final R repository;

    protected AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public E save(E entity) {
        onAfterSave(entity);
        entity = repository.save(entity);
        onAfterSave(entity);
        return entity;
    }

    @Override
    public E saveAndFlush(E entity) {
        onBeforeSave(entity);
        entity = repository.saveAndFlush(entity);
        onAfterSave(entity);
        return entity;
    }

    @Override
    public void delete(E entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<E> findOneById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<E> findByExample(Example<E> ex, Pageable pageable) {
        return repository.findAll(ex, pageable);
    }

    @Override
    public Optional<E> findOneByExample(Example<E> ex) {
        return repository.findOne(ex);
    }

    public E getOne(Long id) {
        return repository.getReferenceById(id);
    }

    public Page<E> findAll(Example<E> ex, Pageable pageable) {
        return repository.findAll(ex, pageable);
    }

    public Page<E> findAll(Specification<E> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    protected boolean currentQueryIsCountRecords(CriteriaQuery<?> criteriaQuery) {
        return criteriaQuery.getResultType() == Long.class
                || criteriaQuery.getResultType() == long.class;
    }

    public long count() {
        return repository.count();
    }
}
