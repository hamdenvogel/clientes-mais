package io.github.hvogel.clientes.infra;

import io.github.hvogel.clientes.test.util.EqualsTestHelper;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class BaseEntityExtraTest {

    static class TestEntity extends BaseEntity {
    }

    @Test
    void testEqualsEdgeCases() {
        TestEntity entity = new TestEntity();
        entity.setId(1);

        // Test standard edge cases
        EqualsTestHelper.assertEqualsEdgeCases(entity);

        // Different ID
        TestEntity other = new TestEntity();
        other.setId(2);
        assertNotEquals(other, entity);

        // Different createdBy
        other.setId(1);
        entity.setCreatedBy("user1");
        other.setCreatedBy("user2");
        assertNotEquals(other, entity);

        // Different createdDate
        other.setCreatedBy("user1");
        entity.setCreatedDate(new Date(1000));
        other.setCreatedDate(new Date(2000));
        assertNotEquals(other, entity);

        // Different status
        other.setCreatedDate(new Date(1000));
        entity.setStatus(true);
        other.setStatus(false);
        assertNotEquals(other, entity);

        // Different updatedBy
        other.setStatus(true);
        entity.setUpdatedBy("admin1");
        other.setUpdatedBy("admin2");
        assertNotEquals(other, entity);

        // Different updatedDate
        other.setUpdatedBy("admin1");
        entity.setUpdatedDate(new Date(1000));
        other.setUpdatedDate(new Date(2000));
        assertNotEquals(other, entity);

        // All same
        other.setUpdatedDate(new Date(1000));
        assertEquals(other, entity);
    }
}
