package io.github.hvogel.clientes.model.specification;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.model.entity.Produto;

public class ProdutoSpecification implements Specification<Produto> {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String DATE_PATTERN_SLASH = "dd/MM/yyyy";
    private static final String DATE_PATTERN_DASH = "dd-MM-yyyy";

    private final List<SearchCriteria> list;

    public ProdutoSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Produto> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : list) {
            Class<?> javaType = resolveJavaType(root, criteria);
            Object val = convertValue(criteria, javaType);

            if (val == null) {
                predicates.add(builder.disjunction());
                continue;
            }

            Predicate predicate = buildPredicate(root, builder, criteria, val, javaType);
            if (predicate != null) {
                predicates.add(predicate);
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Class<?> resolveJavaType(Root<Produto> root, SearchCriteria criteria) {
        return root.getModel().getAttribute(criteria.getKey()).getJavaType();
    }

    private Object convertValue(SearchCriteria criteria, Class<?> originalJavaType) {
        Object val = criteria.getValue();
        if (val instanceof String sVal) {
            return convertStringValue(sVal, originalJavaType, criteria);
        }
        return val;
    }

    private Object convertStringValue(String sVal, Class<?> originalJavaType, SearchCriteria criteria) {
        if (originalJavaType.equals(LocalDate.class)) {
            return isNotDateRangeOperation(criteria) ? parseDate(sVal) : sVal; // Warning: criteria access needs field
                                                                               // or param
        } else if (originalJavaType.equals(BigDecimal.class)) {
            return new BigDecimal(sVal);
        } else if (originalJavaType.equals(Integer.class)) {
            try {
                return Integer.valueOf(sVal);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (originalJavaType.equals(Double.class)) {
            try {
                return Double.valueOf(sVal);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return sVal; // Fallback
    }

    private LocalDate parseDate(String sVal) {
        if (sVal.contains("/")) {
            return LocalDate.parse(sVal, DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH));
        } else {
            try {
                return LocalDate.parse(sVal, DateTimeFormatter.ofPattern(DATE_PATTERN_DASH));
            } catch (java.time.format.DateTimeParseException e) {
                return null;
            }
        }
    }

    private boolean isNotDateRangeOperation(SearchCriteria criteria) {
        return !criteria.getOperation().equals(io.github.hvogel.clientes.enums.SearchOperation.BETWEEN_DATE)
                && !criteria.getOperation().equals(io.github.hvogel.clientes.enums.SearchOperation.BETWEEN_DATETIME);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Predicate buildPredicate(Root<Produto> root, CriteriaBuilder builder, SearchCriteria criteria, Object val,
            Class<?> javaType) {
        switch (criteria.getOperation()) {
            case GREATER_THAN:
                return builder.greaterThan(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case LESS_THAN:
                return builder.lessThan(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case GREATER_THAN_EQUAL:
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case LESS_THAN_EQUAL:
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case NOT_EQUAL:
                return builder.notEqual(root.get(criteria.getKey()), val);
            case EQUAL:
                return builder.equal(root.get(criteria.getKey()), val);
            case LIKE:
                return builder.like(builder.lower(root.get(criteria.getKey()).as(String.class)),
                        "%" + criteria.getValue().toString().toLowerCase() + "%");
            case LIKE_START:
                return builder.like(builder.lower(root.get(criteria.getKey()).as(String.class)),
                        criteria.getValue().toString().toLowerCase() + "%");
            case LIKE_END:
                return builder.like(builder.lower(root.get(criteria.getKey()).as(String.class)),
                        "%" + criteria.getValue().toString().toLowerCase());
            case IN:
                return buildInPredicate(root, builder, criteria);
            case NOT_IN:
                return builder.not(buildInPredicate(root, builder, criteria));
            case BETWEEN_DATE:
                return buildBetweenDatePredicate(root, builder, criteria);
            case BETWEEN_DATETIME:
                return null; // Not implemented as per original
            default:
                return null;
        }
    }

    private Predicate buildInPredicate(Root<Produto> root, CriteriaBuilder builder, SearchCriteria criteria) {
        CriteriaBuilder.In<Object> inClause = builder.in(root.get(criteria.getKey()));
        if (criteria.getValue() instanceof String string && string.contains(",")) {
            for (String v : string.split(",")) {
                inClause.value(v.trim());
            }
        } else {
            inClause.value(criteria.getValue());
        }
        return inClause;
    }

    private Predicate buildBetweenDatePredicate(Root<Produto> root, CriteriaBuilder builder, SearchCriteria criteria) {
        try {
            String[] valores = criteria.getValue().toString().trim().split(";");
            LocalDate startDate = LocalDate.parse(valores[0], DateTimeFormatter.ofPattern(DATE_PATTERN_DASH));
            LocalDate endDate = LocalDate.parse(valores[1], DateTimeFormatter.ofPattern(DATE_PATTERN_DASH));
            return builder.between(root.get(criteria.getKey()).as(LocalDate.class), startDate, endDate);
        } catch (java.time.format.DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            try {
                String[] valores = criteria.getValue().toString().trim().split(";");
                LocalDate startDate = LocalDate.parse(valores[0],
                        DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH));
                LocalDate endDate = LocalDate.parse(valores[1], DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH));
                return builder.between(root.get(criteria.getKey()).as(LocalDate.class), startDate,
                        endDate);
            } catch (java.time.format.DateTimeParseException | ArrayIndexOutOfBoundsException ex) {
                return null;
            }
        }
    }
}
