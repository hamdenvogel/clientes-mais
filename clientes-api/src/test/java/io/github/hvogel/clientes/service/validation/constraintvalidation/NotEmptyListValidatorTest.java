package io.github.hvogel.clientes.service.validation.constraintvalidation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;

class NotEmptyListValidatorTest {

    private NotEmptyListValidator validator = new NotEmptyListValidator();

    @Test
    void testIsValid() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

        assertTrue(validator.isValid(Collections.singletonList("item"), context));

        assertFalse(validator.isValid(null, context));
        assertFalse(validator.isValid(Collections.emptyList(), context));
    }

    @Test
    void testInitialize() {
        // Just to cover the default method if it exists or is overridden
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> validator.initialize(null));
    }
}
