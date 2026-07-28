package io.github.hvogel.clientes.infra;

import io.github.hvogel.clientes.infra.CommonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AbstractControllerTest {

    // Concrete implementations for testing
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
        private Long id;

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    @SuppressWarnings("unchecked")
    static abstract class TestService implements CommonService<TestEntity, TestDTO> {
    }

    static class TestController extends AbstractController<TestEntity, TestDTO, TestService> {
        public TestController(TestService service) {
            super(service);
        }
    }

    private TestService service;
    private TestController controller;

    @BeforeEach
    void setUp() {
        service = mock(TestService.class);
        controller = new TestController(service);
    }

    @Test
    void testFindAll() {
        Pageable pageable = Pageable.unpaged();
        TestEntity entity = new TestEntity();
        TestDTO dto = new TestDTO();
        Page<TestEntity> page = new PageImpl<>(Collections.singletonList(entity));

        when(service.findAll(pageable)).thenReturn(page);
        when(service.convertToDto(entity)).thenReturn(dto);

        Page<TestDTO> result = controller.findAll(pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindOne_Found() {
        TestEntity entity = new TestEntity();
        TestDTO dto = new TestDTO();
        when(service.findOneById(1L)).thenReturn(Optional.of(entity));
        when(service.convertToDto(entity)).thenReturn(dto);

        ResponseEntity<TestDTO> response = controller.findOne(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testFindOne_NotFound() {
        when(service.findOneById(1L)).thenReturn(Optional.empty());

        ResponseEntity<TestDTO> response = controller.findOne(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDelete() {
        ResponseEntity<Void> response = controller.delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteById(1L);
    }

    @Test
    void testInsert() {
        TestDTO dto = new TestDTO();
        TestEntity entity = new TestEntity();
        when(service.convertToEntity(dto)).thenReturn(entity);
        when(service.saveAndFlush(entity)).thenReturn(entity);
        when(service.convertToDto(entity)).thenReturn(dto);

        ResponseEntity<TestDTO> response = controller.insert(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testUpdate_Found() {
        TestDTO dto = new TestDTO();
        TestEntity entity = new TestEntity();
        when(service.findOneById(1L)).thenReturn(Optional.of(entity));
        when(service.convertToEntity(dto)).thenReturn(entity);

        ResponseEntity<TestDTO> response = controller.update(1L, dto);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).save(entity);
    }

    @Test
    void testUpdate_NotFound() {
        TestDTO dto = new TestDTO();
        when(service.findOneById(1L)).thenReturn(Optional.empty());

        ResponseEntity<TestDTO> response = controller.update(1L, dto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testServiceGetter() {
        assertEquals(service, controller.service());
    }
}
