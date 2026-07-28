package io.github.hvogel.clientes.model.specification;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.enums.SearchOperation;
import io.github.hvogel.clientes.model.entity.Prestador;
import io.github.hvogel.clientes.test.base.BaseSpecificationTest;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.metamodel.SingularAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestadorSpecificationTest extends BaseSpecificationTest<Prestador> {

    private PrestadorSpecification specification;

    @BeforeEach
    public void setUp() {
        super.setUp();
        specification = new PrestadorSpecification();
    }

    @Override
    protected Specification<Prestador> getSpecification() {
        return specification;
    }

    @Override
    protected void addCriteria(SearchCriteria criteria) {
        specification.add(criteria);
    }

    @Test
    void testHasNomeLike() {
        var spec = PrestadorSpecification.hasNomeLike("João");
        assertNotNull(spec);
    }

    // Keeping generic tests that might not be in Base or are specific

    @Test
    void testToPredicateBetweenDateTime() {
        setupDateTimeAttributeMocks("dataHora"); // Custom helper or use generic if I added it?
        // I added setupDateTimeAttributeMocks to BaseSpecificationTest. Use it.
        when(path.as(LocalDateTime.class)).thenReturn((Expression) dateTimeExpression);
        when(criteriaBuilder.between(any(Expression.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataHora", "01-01-2024 10:00:00;31-12-2024 18:00:00",
                SearchOperation.BETWEEN_DATETIME);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).between(any(Expression.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testToPredicateBetweenDateTimeWithInvalidFormat() {
        setupDateTimeAttributeMocks("dataHora");
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataHora", "invalid;invalid", SearchOperation.BETWEEN_DATETIME);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }

    @Test
    void testToPredicateBetweenDateWithSingleValue() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "01-01-2024", SearchOperation.BETWEEN_DATE);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }

    @Test
    void testToPredicateWithInvalidDateFormat() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "invaliddate", SearchOperation.EQUAL);
        specification.add(criteria);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }
}
