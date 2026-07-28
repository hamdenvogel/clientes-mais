package io.github.hvogel.clientes.model.specification;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.enums.SearchOperation;
import io.github.hvogel.clientes.model.entity.Cliente;
import io.github.hvogel.clientes.test.base.BaseSpecificationTest;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.metamodel.SingularAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteSpecificationTest extends BaseSpecificationTest<Cliente> {

    private ClienteSpecification specification;

    @BeforeEach
    public void setUp() {
        super.setUp();
        specification = new ClienteSpecification();
    }

    @Override
    protected Specification<Cliente> getSpecification() {
        return specification;
    }

    @Override
    protected void addCriteria(SearchCriteria criteria) {
        specification.add(criteria);
    }

    // Keeping specific tests not in Base

    @Test
    void testToPredicateInvalidInteger() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute("id")).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) Integer.class);
        when(root.get("id")).thenReturn((Path) path);

        // buildPredicate will be called with val="not_number"
        // It falls back to default logic or specific logic depending on how
        // convertValue behaves
        // convertValue catch NumberFormatException and returns value as is
        // ("not_number")
        // Then buildPredicate -> EQUAL -> builder.equal

        when(criteriaBuilder.equal(any(), any())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("id", "not_number", SearchOperation.EQUAL);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).equal(any(), eq("not_number")); // Verify it uses the raw string
    }

    @Test
    void testToPredicateInvalidDateSlash() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute("dataCadastro")).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) LocalDate.class);
        when(root.get("dataCadastro")).thenReturn((Path) path);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        // "invalid/date" contains slash, tries slash pattern -> fails -> returns null
        SearchCriteria criteria = new SearchCriteria("dataCadastro", "invalid/date", SearchOperation.EQUAL);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }

    @Test
    void testToPredicateBetweenDate_Dash_InvalidValues() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute("dataCadastro")).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) LocalDate.class);
        when(root.get("dataCadastro")).thenReturn((Path) path);
        when(criteriaBuilder.and(any())).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "invalid;invalid", SearchOperation.BETWEEN_DATE);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);
        assertNotNull(result);
    }

    @Test
    void testToPredicateWithMultipleCriteria() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute(anyString())).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) String.class);
        when(root.get(anyString())).thenReturn((Path) path);
        when(criteriaBuilder.equal(any(), any())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria1 = new SearchCriteria("nome", "João", SearchOperation.EQUAL);
        SearchCriteria criteria2 = new SearchCriteria("cpf", "12345678901", SearchOperation.EQUAL);
        specification.add(criteria1);
        specification.add(criteria2);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder, atLeast(2)).equal(any(), anyString());
    }
}
