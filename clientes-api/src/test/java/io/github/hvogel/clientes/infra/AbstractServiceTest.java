package io.github.hvogel.clientes.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractServiceTest {

    static class TestEntity implements IBaseEntity, Serializable {
        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }
    }

    static class TestDTO implements Serializable {
    }

    interface TestRepository extends CommonRepository<TestEntity> {
    }

    static class TestService extends AbstractService<TestEntity, TestDTO, TestRepository> {
        public TestService(TestRepository repository) {
            super(repository);
        }

        @Override
        public TestDTO convertToDto(TestEntity entity) {
            return new TestDTO();
        }

        @Override
        public TestEntity convertToEntity(TestDTO dto) {
            return new TestEntity();
        }
    }

    private TestRepository repository;
    private TestService service;

    @BeforeEach
    void setUp() {
        repository = mock(TestRepository.class);
        service = new TestService(repository);
    }

    @Test
    void testSave() {
        TestEntity entity = new TestEntity();
        when(repository.save(entity)).thenReturn(entity);
        TestEntity saved = service.save(entity);
        assertNotNull(saved);
        verify(repository).save(entity);
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<TestEntity> list = service.findAll();
        assertNotNull(list);
        verify(repository).findAll();
    }

    @Test
    void testFindOneById() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<TestEntity> result = service.findOneById(1L);
        assertFalse(result.isPresent());
        verify(repository).findById(1L);
    }

    @Test
    void testDelete() {
        TestEntity entity = new TestEntity();
        service.delete(entity);
        verify(repository).delete(entity);
    }

    @Test
    void testDeleteById() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testSaveAndFlush() {
        TestEntity entity = new TestEntity();
        when(repository.saveAndFlush(entity)).thenReturn(entity);
        TestEntity saved = service.saveAndFlush(entity);
        assertNotNull(saved);
        verify(repository).saveAndFlush(entity);
    }

    @Test
    void testFindAllPageable() {
        Pageable pageable = Pageable.unpaged();
        when(repository.findAll(pageable)).thenReturn(Page.empty());
        Page<TestEntity> page = service.findAll(pageable);
        assertNotNull(page);
        verify(repository).findAll(pageable);
    }

    @Test
    void testGetOne() {
        TestEntity entity = new TestEntity();
        when(repository.getReferenceById(1L)).thenReturn(entity);
        TestEntity result = service.getOne(1L);
        assertNotNull(result);
        verify(repository).getReferenceById(1L);
    }

    @Test
    void testCount() {
        when(repository.count()).thenReturn(10L);
        long count = service.count();
        assertEquals(10L, count);
        verify(repository).count();
    }

    @Test
    void testFindByExample() {
        org.springframework.data.domain.Example<TestEntity> example = org.springframework.data.domain.Example
                .of(new TestEntity());
        Pageable pageable = Pageable.unpaged();
        when(repository.findAll(example, pageable)).thenReturn(Page.empty());

        Page<TestEntity> result = service.findByExample(example, pageable);
        assertNotNull(result);
        verify(repository).findAll(example, pageable);
    }

    @Test
    void testFindOneByExample() {
        org.springframework.data.domain.Example<TestEntity> example = org.springframework.data.domain.Example
                .of(new TestEntity());
        when(repository.findOne(example)).thenReturn(Optional.empty());

        Optional<TestEntity> result = service.findOneByExample(example);
        assertFalse(result.isPresent());
        verify(repository).findOne(example);
    }

    @Test
    void testFindAllExamplePageable() {
        org.springframework.data.domain.Example<TestEntity> example = org.springframework.data.domain.Example
                .of(new TestEntity());
        Pageable pageable = Pageable.unpaged();
        when(repository.findAll(example, pageable)).thenReturn(Page.empty());

        Page<TestEntity> result = service.findAll(example, pageable);
        assertNotNull(result);
        verify(repository).findAll(example, pageable);
    }

    @Test
    void testFindAllSpecPageable() {
        org.springframework.data.jpa.domain.Specification<TestEntity> spec = (root, query, cb) -> null;
        Pageable pageable = Pageable.unpaged();
        when(repository.findAll(spec, pageable)).thenReturn(Page.empty());

        Page<TestEntity> result = service.findAll(spec, pageable);
        assertNotNull(result);
        verify(repository).findAll(spec, pageable);
    }

    @Test
    void testCurrentQueryIsCountRecords() {
        jakarta.persistence.criteria.CriteriaQuery<?> queryLong = mock(
                jakarta.persistence.criteria.CriteriaQuery.class);
        doReturn(Long.class).when(queryLong).getResultType();
        assertTrue(service.currentQueryIsCountRecords(queryLong));

        jakarta.persistence.criteria.CriteriaQuery<?> queryPrimLong = mock(
                jakarta.persistence.criteria.CriteriaQuery.class);
        doReturn(long.class).when(queryPrimLong).getResultType();
        assertTrue(service.currentQueryIsCountRecords(queryPrimLong));

        jakarta.persistence.criteria.CriteriaQuery<?> queryOther = mock(
                jakarta.persistence.criteria.CriteriaQuery.class);
        doReturn(Object.class).when(queryOther).getResultType();
        assertFalse(service.currentQueryIsCountRecords(queryOther));
    }
}
