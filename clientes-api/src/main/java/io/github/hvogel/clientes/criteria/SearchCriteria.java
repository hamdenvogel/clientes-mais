package io.github.hvogel.clientes.criteria;

import java.io.Serial;
import java.io.Serializable;

import io.github.hvogel.clientes.enums.SearchOperation;

public class SearchCriteria implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String key;
    private Serializable value;
    private SearchOperation operation;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, Serializable value, SearchOperation operation) {
        this.key = key;
        this.value = value;
        this.operation = operation;
    }

    // getters and setters, equals(), toString(), ... (omitted for brevity)

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }

    public SearchOperation getOperation() {
        return operation;
    }

    public void setOperation(SearchOperation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", operation=" + operation +
                '}';
    }

}
