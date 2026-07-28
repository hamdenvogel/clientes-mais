package io.github.hvogel.clientes.model.specification;

import java.io.Serial;
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
import io.github.hvogel.clientes.model.entity.Cliente;

public class ClienteSpecification implements Specification<Cliente> {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String DATA_CADASTRO = "dataCadastro";
    private static final String DATE_PATTERN_SLASH = "dd/MM/yyyy";
    private static final String DATE_PATTERN_DASH = "dd-MM-yyyy";

    private final List<SearchCriteria> list;

    public ClienteSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {
            Class<?> javaType = resolveJavaType(root, criteria);
            Object val = convertValue(criteria, javaType);

            // Update javaType if it was forced to LocalDate during conversion
            if (val instanceof LocalDate) {
                javaType = LocalDate.class;
            }

            predicates.add(buildPredicate(root, builder, criteria, val, javaType));
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Class<?> resolveJavaType(Root<Cliente> root, SearchCriteria criteria) {
        return root.getModel().getAttribute(criteria.getKey()).getJavaType();
    }

    private Object convertValue(SearchCriteria criteria, Class<?> originalJavaType) {
        Object val = criteria.getValue();
        if (val instanceof String sVal) {
            if (originalJavaType.equals(LocalDate.class) || criteria.getKey().equals(DATA_CADASTRO)) {
                return isNotDateRangeOperation(criteria) ? parseDate(sVal) : sVal;
            }
            if (originalJavaType.equals(Integer.class)) {
                try {
                    return Integer.valueOf(sVal);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return val;
    }

    private boolean isNotDateRangeOperation(SearchCriteria criteria) {
        return !criteria.getOperation().equals(io.github.hvogel.clientes.enums.SearchOperation.BETWEEN_DATE);
    }

    private LocalDate parseDate(String sVal) {
        if (sVal.contains("/")) {
            try {
                return LocalDate.parse(sVal, DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH));
            } catch (java.time.format.DateTimeParseException e) {
                return null;
            }
        } else {
            try {
                return LocalDate.parse(sVal, DateTimeFormatter.ofPattern(DATE_PATTERN_DASH));
            } catch (java.time.format.DateTimeParseException e) {
                return null;
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Predicate buildPredicate(Root<Cliente> root, CriteriaBuilder builder, SearchCriteria criteria, Object val,
            Class<?> javaType) {
        switch (criteria.getOperation()) {
            case GREATER_THAN:
                if (criteria.getKey().equals(DATA_CADASTRO) && val instanceof LocalDate localDate) {
                    return builder.greaterThan(root.get(DATA_CADASTRO), localDate);
                }
                return builder.greaterThan(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case LESS_THAN:
                if (criteria.getKey().equals(DATA_CADASTRO) && val instanceof LocalDate localDate) {
                    return builder.lessThan(root.get(DATA_CADASTRO), localDate);
                }
                return builder.lessThan(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case GREATER_THAN_EQUAL:
                if (criteria.getKey().equals(DATA_CADASTRO) && val instanceof LocalDate localDate) {
                    return builder.greaterThanOrEqualTo(root.get(DATA_CADASTRO), localDate);
                }
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case LESS_THAN_EQUAL:
                if (criteria.getKey().equals(DATA_CADASTRO) && val instanceof LocalDate localDate) {
                    return builder.lessThanOrEqualTo(root.get(DATA_CADASTRO), localDate);
                }
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case NOT_EQUAL:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case EQUAL:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
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
                return builder.not(buildInPredicate(root, builder, criteria)); // Reuse buildInPredicate logic
                                                                               // effectively
            case BETWEEN_DATE:
                return buildBetweenDatePredicate(root, builder, criteria);
            default:
                return null;
        }
    }

    private Predicate buildInPredicate(Root<Cliente> root, CriteriaBuilder builder, SearchCriteria criteria) {
        CriteriaBuilder.In<Object> inClause = builder.in(root.get(criteria.getKey()));
        if (criteria.getValue() instanceof String s && s.contains(",")) {
            for (String v : s.split(",")) {
                inClause.value(v.trim());
            }
        } else {
            inClause.value(criteria.getValue());
        }
        return inClause;
    }

    private Predicate buildBetweenDatePredicate(Root<Cliente> root, CriteriaBuilder builder, SearchCriteria criteria) {
        try {
            String[] valores = criteria.getValue().toString().trim().split(";");
            LocalDate startDate = LocalDate.parse(valores[0], DateTimeFormatter.ofPattern(DATE_PATTERN_DASH));
            LocalDate endDate = LocalDate.parse(valores[1], DateTimeFormatter.ofPattern(DATE_PATTERN_DASH));
            return builder.between(root.get(criteria.getKey()).as(LocalDate.class), startDate, endDate);
        } catch (java.time.format.DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            try {
                String[] valores = criteria.getValue().toString().trim().split(";");
                LocalDate startDate = LocalDate.parse(valores[0], DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH));
                LocalDate endDate = LocalDate.parse(valores[1], DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH));
                return builder.between(root.get(criteria.getKey()).as(LocalDate.class), startDate, endDate);
            } catch (java.time.format.DateTimeParseException | ArrayIndexOutOfBoundsException ex) {
                return null;
            }
        }
    }
}
