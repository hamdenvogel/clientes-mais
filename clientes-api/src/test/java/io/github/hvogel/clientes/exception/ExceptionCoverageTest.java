package io.github.hvogel.clientes.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExceptionCoverageTest {

    @Test
    void testAllCustomExceptions() {
        final String exceptionPackage = "io.github.hvogel.clientes.exception";

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Exception.class));
        provider.addIncludeFilter(new AssignableTypeFilter(RuntimeException.class));

        Set<BeanDefinition> beans = provider.findCandidateComponents(exceptionPackage);

        for (BeanDefinition bean : beans) {
            try {
                Class<?> clazz = Class.forName(bean.getBeanClassName());
                testExceptionClass(clazz);
            } catch (ClassNotFoundException e) {
                // Ignore
            }
        }
    }

    private void testExceptionClass(Class<?> clazz) {
        assertDoesNotThrow(() -> {
            // Try default constructor
            try {
                Constructor<?> constructor = clazz.getConstructor();
                constructor.newInstance();
            } catch (NoSuchMethodException e) {
                // ignore
            }

            // Try message constructor
            try {
                Constructor<?> constructor = clazz.getConstructor(String.class);
                constructor.newInstance("Test Message");
            } catch (NoSuchMethodException e) {
                // ignore
            }
        });
    }
}
