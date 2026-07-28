package io.github.hvogel.clientes.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternacionalizacaoConfigTest {

    @Test
    void testMessageSource() {
        InternacionalizacaoConfig config = new InternacionalizacaoConfig();
        MessageSource messageSource = config.messageSource();

        assertNotNull(messageSource);
        assertTrue(messageSource instanceof ReloadableResourceBundleMessageSource);
    }

    @Test
    void testValidatorFactoryBean() {
        InternacionalizacaoConfig config = new InternacionalizacaoConfig();
        LocalValidatorFactoryBean validator = config.validatorFactoryBean();

        assertNotNull(validator);
    }
}
