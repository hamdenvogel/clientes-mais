package io.github.hvogel.clientes.test.base;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.enums.SearchOperation;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe base abstrata para testes de Specification.
 * Elimina duplicação de código nos testes de ClienteSpecification,
 * PrestadorSpecification, etc.
 *
 * @param <T> O tipo da entidade sendo testada
 */
public abstract class BaseSpecificationTest<T> {

    @Mock
    protected Root<T> root;

    @Mock
    protected CriteriaQuery<?> query;

    @Mock
    protected CriteriaBuilder criteriaBuilder;

    @Mock
    protected EntityType<T> entityType;

    @Mock
    protected SingularAttribute<T, ?> attribute;

    @Mock
    protected Path<Object> path;

    @Mock
    protected Path<String> stringExpression;
    @Mock
    protected Path<LocalDate> dateExpression;

    @Mock
    protected Path<LocalDateTime> dateTimeExpression;

    @Mock
    protected Predicate predicate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Retorna a especificação que está sendo testada.
     * Deve ser implementado pelas subclasses.
     */
    protected abstract org.springframework.data.jpa.domain.Specification<T> getSpecification();

    /**
     * Adiciona um critério à especificação.
     */
    protected abstract void addCriteria(SearchCriteria criteria);

    /**
     * Retorna o nome do campo padrão para testes (geralmente "nome").
     */
    protected String getDefaultFieldName() {
        return "nome";
    }

    /**
     * Teste padrão para o método add().
     */
    @Test
    void testAdd() {
        SearchCriteria criteria = new SearchCriteria(getDefaultFieldName(), "Test", SearchOperation.EQUAL);
        addCriteria(criteria);
        assertNotNull(getSpecification());
    }

    /**
     * Helper method para configurar mocks padrão de String
     */
    protected void setupStringAttributeMocks() {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute(getDefaultFieldName())).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) String.class);
        when(root.get(getDefaultFieldName())).thenReturn((Path) stringExpression);
        when(stringExpression.as(String.class)).thenReturn((Expression) stringExpression);
    }

    /**
     * Helper method para configurar mocks padrão de Date
     */
    protected void setupDateAttributeMocks(String fieldName) {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute(fieldName)).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) LocalDate.class);
        when(root.get(fieldName)).thenReturn((Path) dateExpression);
        when(dateExpression.as(LocalDate.class)).thenReturn((Expression) dateExpression);
    }

    /**
     * Helper method para configurar mocks padrão de LocalDateTime
     */
    protected void setupDateTimeAttributeMocks(String fieldName) {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute(fieldName)).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) LocalDateTime.class);
        when(root.get(fieldName)).thenReturn((Path) dateTimeExpression);
        when(dateTimeExpression.as(LocalDateTime.class)).thenReturn((Expression) dateTimeExpression);
    }

    /**
     * Helper method para configurar mocks padrão de Integer
     */
    protected void setupIntegerAttributeMocks(String fieldName) {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute(fieldName)).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) Integer.class);
        when(root.get(fieldName)).thenReturn((Path) path);
    }

    @Test
    void testToPredicateEqual() {
        setupStringAttributeMocks();
        when(criteriaBuilder.equal(any(), any())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria(getDefaultFieldName(), "João", SearchOperation.EQUAL);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder, atLeastOnce()).equal(any(), anyString());
    }

    @Test
    void testToPredicateLike() {
        setupStringAttributeMocks();
        when(criteriaBuilder.lower(any(Expression.class))).thenReturn(stringExpression);
        when(criteriaBuilder.like(any(Expression.class), anyString())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria(getDefaultFieldName(), "João", SearchOperation.LIKE);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).like(any(Expression.class), anyString());
    }

    @Test
    void testToPredicateLikeStart() {
        setupStringAttributeMocks();
        when(criteriaBuilder.lower(any(Expression.class))).thenReturn(stringExpression);
        when(criteriaBuilder.like(any(Expression.class), anyString())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria(getDefaultFieldName(), "João", SearchOperation.LIKE_START);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).like(any(Expression.class), contains("joão"));
    }

    @Test
    void testToPredicateLikeEnd() {
        setupStringAttributeMocks();
        when(criteriaBuilder.lower(any(Expression.class))).thenReturn(stringExpression);
        when(criteriaBuilder.like(any(Expression.class), anyString())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria(getDefaultFieldName(), "João", SearchOperation.LIKE_END);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).like(any(Expression.class), contains("%joão"));
    }

    @Test
    void testToPredicateNotEqual() {
        setupStringAttributeMocks();
        when(criteriaBuilder.notEqual(any(), any())).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria(getDefaultFieldName(), "João", SearchOperation.NOT_EQUAL);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder, atLeastOnce()).notEqual(any(), anyString());
    }

    @Test
    void testToPredicateGreaterThan() {
        setupIntegerAttributeMocks("id");
        when(path.as(any(Class.class))).thenReturn((Expression) path);
        when(criteriaBuilder.greaterThan(any(Expression.class), any(Comparable.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("id", "10", SearchOperation.GREATER_THAN);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).greaterThan(any(Expression.class), any(Comparable.class));
    }

    @Test
    void testToPredicateLessThan() {
        setupIntegerAttributeMocks("id");
        when(path.as(any(Class.class))).thenReturn((Expression) path);
        when(criteriaBuilder.lessThan(any(Expression.class), any(Comparable.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("id", "10", SearchOperation.LESS_THAN);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).lessThan(any(Expression.class), any(Comparable.class));
    }

    @Test
    void testToPredicateGreaterThanEqual() {
        setupIntegerAttributeMocks("id");
        when(path.as(any(Class.class))).thenReturn((Expression) path);
        when(criteriaBuilder.greaterThanOrEqualTo(any(Expression.class), any(Comparable.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("id", "10", SearchOperation.GREATER_THAN_EQUAL);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).greaterThanOrEqualTo(any(Expression.class), any(Comparable.class));
    }

    @Test
    void testToPredicateLessThanEqual() {
        setupIntegerAttributeMocks("id");
        when(path.as(any(Class.class))).thenReturn((Expression) path);
        when(criteriaBuilder.lessThanOrEqualTo(any(Expression.class), any(Comparable.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("id", "10", SearchOperation.LESS_THAN_EQUAL);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).lessThanOrEqualTo(any(Expression.class), any(Comparable.class));
    }

    @Test
    void testToPredicateIn() {
        setupStringAttributeMocks("status");
        CriteriaBuilder.In<Object> inClause = mock(CriteriaBuilder.In.class);
        when(criteriaBuilder.in(any(Expression.class))).thenReturn(inClause);
        when(inClause.value(any())).thenReturn(inClause);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("status", "A,I,E", SearchOperation.IN);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(inClause, times(3)).value(anyString());
    }

    @Test
    void testToPredicateInSingleValue() {
        setupStringAttributeMocks("status");
        CriteriaBuilder.In<Object> inClause = mock(CriteriaBuilder.In.class);
        when(criteriaBuilder.in(any(Expression.class))).thenReturn(inClause);
        when(inClause.value(any())).thenReturn(inClause);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("status", "A", SearchOperation.IN);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(inClause).value("A");
    }

    @Test
    void testToPredicateNotIn() {
        setupStringAttributeMocks("status");
        CriteriaBuilder.In<Object> inClause = mock(CriteriaBuilder.In.class);
        when(criteriaBuilder.in(any(Expression.class))).thenReturn(inClause);
        when(inClause.value(any())).thenReturn(inClause);
        when(criteriaBuilder.not(any(Predicate.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("status", "C", SearchOperation.NOT_IN);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).not(any(Predicate.class));
    }

    @Test
    void testToPredicateBetweenDate() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.between(any(Expression.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "01-01-2024;31-12-2024",
                SearchOperation.BETWEEN_DATE);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).between(any(Expression.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testToPredicateBetweenDateWithSlashFormat() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.between(any(Expression.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "01/01/2024;31/12/2024",
                SearchOperation.BETWEEN_DATE);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).between(any(Expression.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testToPredicateBetweenDateWithInvalidFormat() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "invalid;invalid", SearchOperation.BETWEEN_DATE);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }

    @Test
    void testToPredicateWithInvalidDateFormat() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "invaliddate", SearchOperation.EQUAL);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
    }

    @Test
    void testToPredicateGreaterThanWithDataCadastro() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.greaterThan(any(Expression.class), any(LocalDate.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "01/01/2024", SearchOperation.GREATER_THAN);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).greaterThan(any(Expression.class), any(LocalDate.class));
    }

    @Test
    void testToPredicateLessThanWithDataCadastro() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.lessThan(any(Expression.class), any(LocalDate.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "31-12-2024", SearchOperation.LESS_THAN);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).lessThan(any(Expression.class), any(LocalDate.class));
    }

    @Test
    void testToPredicateGreaterThanEqualWithDataCadastro() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.greaterThanOrEqualTo(any(Expression.class), any(LocalDate.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "01/01/2024", SearchOperation.GREATER_THAN_EQUAL);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).greaterThanOrEqualTo(any(Expression.class), any(LocalDate.class));
    }

    @Test
    void testToPredicateLessThanEqualWithDataCadastro() {
        setupDateAttributeMocks("dataCadastro");
        when(criteriaBuilder.lessThanOrEqualTo(any(Expression.class), any(LocalDate.class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        SearchCriteria criteria = new SearchCriteria("dataCadastro", "31-12-2024", SearchOperation.LESS_THAN_EQUAL);
        addCriteria(criteria);

        Predicate result = getSpecification().toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).lessThanOrEqualTo(any(Expression.class), any(LocalDate.class));
    }

    /**
     * Helper method wrapper for custom field names
     */
    protected void setupStringAttributeMocks(String fieldName) {
        when(root.getModel()).thenReturn(entityType);
        when(entityType.getAttribute(fieldName)).thenReturn((SingularAttribute) attribute);
        when(attribute.getJavaType()).thenReturn((Class) String.class);
        when(root.get(fieldName)).thenReturn((Path) stringExpression);
        when(stringExpression.as(String.class)).thenReturn((Expression) stringExpression);
    }
}
