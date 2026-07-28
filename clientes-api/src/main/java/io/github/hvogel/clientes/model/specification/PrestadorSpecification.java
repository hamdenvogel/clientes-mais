package io.github.hvogel.clientes.model.specification;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import io.github.hvogel.clientes.criteria.SearchCriteria;
import io.github.hvogel.clientes.model.entity.Prestador;

public class PrestadorSpecification implements Specification<Prestador> {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PrestadorSpecification.class);
    private static final String DATA_CADASTRO = "dataCadastro";
    private static final String DATE_PATTERN_SLASH = "dd/MM/yyyy";
    private static final String DATE_PATTERN_DASH = "dd-MM-yyyy";
    private static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm:ss";

    private final List<SearchCriteria> list;

    public PrestadorSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    public static Specification<Prestador> hasNomeLike(String nome) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.<String>get("nome"), "%" + nome + "%");
    }

    @Override
    public Predicate toPredicate(Root<Prestador> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : list) {
            Class<?> javaType = resolveJavaType(root, criteria);
            Object val = convertValue(criteria, javaType);

            // Update javaType if forced to LocalDate
            if (val instanceof LocalDate) {
                javaType = LocalDate.class;
            }

            if (val == null) {
                // Invalid value for type, return always false
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

    private Class<?> resolveJavaType(Root<Prestador> root, SearchCriteria criteria) {
        return root.getModel().getAttribute(criteria.getKey()).getJavaType();
    }

    private Object convertValue(SearchCriteria criteria, Class<?> originalJavaType) {
        Object val = criteria.getValue();
        if (val instanceof String sVal) {
            if (originalJavaType.equals(LocalDate.class) || criteria.getKey().equals(DATA_CADASTRO)) {
                return isNotDateRangeOperation(criteria) ? parseDate(sVal) : sVal;
            } else if (originalJavaType.equals(LocalDateTime.class)) {
                return val;
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
        return !criteria.getOperation().equals(io.github.hvogel.clientes.enums.SearchOperation.BETWEEN_DATE)
                && !criteria.getOperation().equals(io.github.hvogel.clientes.enums.SearchOperation.BETWEEN_DATETIME);
    }

    private LocalDate parseDate(String sVal) {
        if (sVal.contains("/")) {
            return LocalDate.parse(sVal, DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH));
        } else {
            try {
                return LocalDate.parse(sVal, DateTimeFormatter.ofPattern(DATE_PATTERN_DASH));
            } catch (DateTimeParseException e) {
                return null;
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Predicate buildPredicate(Root<Prestador> root, CriteriaBuilder builder, SearchCriteria criteria, Object val,
            Class<?> javaType) {
        switch (criteria.getOperation()) {
            case GREATER_THAN:
                if (criteria.getKey().equals(DATA_CADASTRO) && val instanceof LocalDate localDate) {
                    return builder.greaterThan(root.get(DATA_CADASTRO).as(LocalDate.class), localDate);
                }
                return builder.greaterThan(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case LESS_THAN:
                if (criteria.getKey().equals(DATA_CADASTRO) && val instanceof LocalDate localDate) {
                    return builder.lessThan(root.get(DATA_CADASTRO).as(LocalDate.class), localDate);
                }
                return builder.lessThan(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case GREATER_THAN_EQUAL:
                if (criteria.getKey().equals(DATA_CADASTRO) && val instanceof LocalDate localDate) {
                    return builder.greaterThanOrEqualTo(root.get(DATA_CADASTRO).as(LocalDate.class), localDate);
                }
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()).as((Class) javaType), (Comparable) val);
            case LESS_THAN_EQUAL:
                if (criteria.getKey().equals(DATA_CADASTRO) && val instanceof LocalDate localDate) {
                    return builder.lessThanOrEqualTo(root.get(DATA_CADASTRO).as(LocalDate.class), localDate);
                }
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
                return buildBetweenDateTimePredicate(root, builder, criteria);
            default:
                return null;
        }
    }

    private Predicate buildInPredicate(Root<Prestador> root, CriteriaBuilder builder, SearchCriteria criteria) {
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

    private Predicate buildBetweenDatePredicate(Root<Prestador> root, CriteriaBuilder builder,
            SearchCriteria criteria) {
        String[] valores = criteria.getValue().toString().trim().split(";");
        if (valores.length < 2) {
            return null;
        }

        DateTimeFormatter formatterDash = DateTimeFormatter.ofPattern(DATE_PATTERN_DASH);
        DateTimeFormatter formatterSlash = DateTimeFormatter.ofPattern(DATE_PATTERN_SLASH);

        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(valores[0], formatterDash);
            endDate = LocalDate.parse(valores[1], formatterDash);
        } catch (DateTimeParseException e) {
            try {
                startDate = LocalDate.parse(valores[0], formatterSlash);
                endDate = LocalDate.parse(valores[1], formatterSlash);
            } catch (DateTimeParseException ex) {
                return null;
            }
        }

        return builder.between(root.get(criteria.getKey()).as(LocalDate.class), startDate, endDate);
    }

    private Predicate buildBetweenDateTimePredicate(Root<Prestador> root, CriteriaBuilder builder,
            SearchCriteria criteria) {
        try {
            String[] valoresDT = criteria.getValue().toString().trim().split(";");
            LocalDateTime startDateDT = LocalDateTime.parse(valoresDT[0],
                    DateTimeFormatter.ofPattern(DATETIME_PATTERN));
            LocalDateTime endDateDT = LocalDateTime.parse(valoresDT[1], DateTimeFormatter.ofPattern(DATETIME_PATTERN));
            return builder.between(root.get(criteria.getKey()).as(LocalDateTime.class), startDateDT, endDateDT);
        } catch (DateTimeParseException e) {
            logger.error("Error parsing date time range", e);
            return null;
        }
    }
}
