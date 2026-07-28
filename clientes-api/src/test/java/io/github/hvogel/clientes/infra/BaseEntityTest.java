package io.github.hvogel.clientes.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;

import org.junit.jupiter.api.Test;

class BaseEntityTest {

    static class TestEntity extends BaseEntity {
    }

    @Test
    void testGettersAndSetters() {
        TestEntity entity = new TestEntity();

        entity.setId(1);
        entity.setCreatedBy("user");
        Date now = new Date();
        entity.setCreatedDate(now);
        entity.setUpdatedBy("admin");
        entity.setUpdatedDate(now);
        entity.setStatus(false);

        assertEquals(1, entity.getId());
        assertEquals("user", entity.getCreatedBy());
        assertEquals(now, entity.getCreatedDate());
        assertEquals("admin", entity.getUpdatedBy());
        assertEquals(now, entity.getUpdatedDate());
        assertFalse(entity.isStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        TestEntity entity1 = new TestEntity();
        entity1.setId(1);
        entity1.setCreatedBy("user");

        TestEntity entity2 = new TestEntity();
        entity2.setId(1);
        entity2.setCreatedBy("user");

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());

        entity2.setId(2);
        assertNotEquals(entity1, entity2);
        assertNotEquals(entity1.hashCode(), entity2.hashCode());

        // Test different classes
        assertNotEquals(entity1, new Object());
    }

    @Test
    void testToString() {
        TestEntity entity = new TestEntity();
        entity.setId(1);
        entity.setCreatedBy("user");

        String stringRepresentation = entity.toString();
        assertTrue(stringRepresentation.contains("id=1"));
        assertTrue(stringRepresentation.contains("createdBy=user"));
    }
}
