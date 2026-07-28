package io.github.hvogel.clientes.criteria;

import io.github.hvogel.clientes.enums.SearchOperation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchCriteriaTest {

    @Test
    void testConstructorsAndGettersSetters() {
        SearchCriteria criteria = new SearchCriteria("key", "value", SearchOperation.EQUAL);

        assertEquals("key", criteria.getKey());
        assertEquals("value", criteria.getValue());
        assertEquals(SearchOperation.EQUAL, criteria.getOperation());

        criteria.setKey("newKey");
        criteria.setValue("newValue");
        criteria.setOperation(SearchOperation.GREATER_THAN);

        assertEquals("newKey", criteria.getKey());
        assertEquals("newValue", criteria.getValue());
        assertEquals(SearchOperation.GREATER_THAN, criteria.getOperation());

        SearchCriteria emptyCriteria = new SearchCriteria();
        assertNull(emptyCriteria.getKey());
    }

    @Test
    void testToString() {
        SearchCriteria criteria = new SearchCriteria("key", "value", SearchOperation.EQUAL);
        assertNotNull(criteria.toString());
        assertTrue(criteria.toString().contains("key"));
        assertTrue(criteria.toString().contains("value"));
    }
}
