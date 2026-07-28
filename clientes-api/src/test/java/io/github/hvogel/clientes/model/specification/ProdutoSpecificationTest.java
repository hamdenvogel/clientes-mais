package io.github.hvogel.clientes.model.specification;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.enums.SearchOperation;
import io.github.hvogel.clientes.model.entity.Produto;
import io.github.hvogel.clientes.test.base.BaseSpecificationTest;
import io.github.hvogel.clientes.test.base.BaseSpecificationTest;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.metamodel.SingularAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoSpecificationTest extends BaseSpecificationTest<Produto> {

    private ProdutoSpecification specification;

    @BeforeEach
    public void setUp() {
        super.setUp();
        specification = new ProdutoSpecification();
    }

    @Override
    protected Specification<Produto> getSpecification() {
        return specification;
    }

    @Override
    protected void addCriteria(SearchCriteria criteria) {
        specification.add(criteria);
    }

    @Test
    void testToPredicateWithBigDecimal() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute("preco")).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) BigDecimal.class);
        when(root.get("preco")).thenReturn((Path) path);
        when(path.as(any(Class.class))).thenReturn((Expression) path);
        when(criteriaBuilder.greaterThan(any(Expression.class), any(Comparable.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("preco", "100.50", SearchOperation.GREATER_THAN);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).greaterThan(any(Expression.class), any(Comparable.class));
    }

    @Test
    void testToPredicateWithDouble() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute("peso")).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) Double.class);
        when(root.get("peso")).thenReturn((Path) path);
        when(path.as(any(Class.class))).thenReturn((Expression) path);
        when(criteriaBuilder.lessThan(any(Expression.class), any(Comparable.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("peso", "50.5", SearchOperation.LESS_THAN);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).lessThan(any(Expression.class), any(Comparable.class));
    }

    @Test
    void testToPredicateBetweenDateTime() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute("dataHora")).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) String.class);
        when(root.get("dataHora")).thenReturn((Path) path);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataHora", "01-01-2024 10:00:00;31-12-2024 18:00:00",
                SearchOperation.BETWEEN_DATETIME);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }

    @Test
    void testToPredicateWithLocalDateAndSlashFormat() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute("dataCriacao")).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) LocalDate.class);
        when(root.get("dataCriacao")).thenReturn((Path) path);
        when(criteriaBuilder.equal(any(), any())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCriacao", "01/01/2024", SearchOperation.EQUAL);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }

    @Test
    void testToPredicateWithLocalDateAndDashFormat() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute("dataCriacao")).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) LocalDate.class);
        when(root.get("dataCriacao")).thenReturn((Path) path);
        when(criteriaBuilder.equal(any(), any())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCriacao", "01-01-2024", SearchOperation.EQUAL);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }
}
