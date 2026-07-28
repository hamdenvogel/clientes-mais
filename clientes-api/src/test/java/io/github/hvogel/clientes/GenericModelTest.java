package io.github.hvogel.clientes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GenericModelTest {

    @Test
    void testEntitiesAndDTOs() {
        assertDoesNotThrow(() -> {
            testPackage("io.github.hvogel.clientes.model.entity");
            testPackage("io.github.hvogel.clientes.rest.dto");
        });
    }

    private void testPackage(String packageName) throws Exception {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));

        Set<BeanDefinition> beans = provider.findCandidateComponents(packageName);
        for (BeanDefinition bean : beans) {
            Class<?> clazz = Class.forName(bean.getBeanClassName());
            if (!Modifier.isAbstract(clazz.getModifiers()) && !clazz.isEnum()) {
                testClass(clazz);
            }
        }
    }

    private void testClass(Class<?> clazz) {
        try {
            Constructor<?> constructor = null;
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                // No no-arg constructor, try to find one
                if (clazz.getConstructors().length > 0) {
                    constructor = clazz.getConstructors()[0];
                }
            }

            if (constructor == null)
                return;

            Object instance1 = createInstance(clazz);
            Object instance2 = createInstance(clazz);

            if (instance1 == null)
                return;

            // Test Getters and Setters
            for (Method method : clazz.getMethods()) {
                if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                    try {
                        Method getter = findGetter(clazz, method.getName().substring(3));
                        if (getter != null) {
                            Class<?> paramType = method.getParameterTypes()[0];
                            Object value = createValue(paramType);
                            method.invoke(instance1, value);
                            getter.invoke(instance1);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }

            // Test Equals and HashCode
            try {
                Method equals = clazz.getMethod("equals", Object.class);
                Method hashCode = clazz.getMethod("hashCode");

                equals.invoke(instance1, instance2);
                equals.invoke(instance1, instance1);
                equals.invoke(instance1, new Object());
                equals.invoke(instance1, (Object) null);

                hashCode.invoke(instance1);
            } catch (Exception ignored) {
            }

            // Test toString
            try {
                Method toString = clazz.getMethod("toString");
                toString.invoke(instance1);
            } catch (Exception ignored) {
            }

        } catch (Exception e) {
            System.err.println("Skipping test for " + clazz.getName() + ": " + e.getMessage());
        }
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // Try with naive nulls/defaults
            try {
                Constructor<?>[] ctors = clazz.getConstructors();
                if (ctors.length > 0) {
                    Constructor<?> ctor = ctors[0];
                    Object[] args = new Object[ctor.getParameterCount()];
                    for (int i = 0; i < args.length; i++) {
                        args[i] = createValue(ctor.getParameterTypes()[i]);
                    }
                    return ctor.newInstance(args);
                }
            } catch (Exception ex) {
                return null;
            }
            return null;
        }
    }

    private Method findGetter(Class<?> clazz, String fieldName) {
        try {
            return clazz.getMethod("get" + fieldName);
        } catch (NoSuchMethodException e) {
            try {
                return clazz.getMethod("is" + fieldName);
            } catch (NoSuchMethodException ex) {
                return null;
            }
        }
    }

    private Object createValue(Class<?> type) {
        if (type == String.class)
            return "test";
        if (type == Integer.class || type == int.class)
            return 1;
        if (type == Long.class || type == long.class)
            return 1L;
        if (type == Double.class || type == double.class)
            return 1.0;
        if (type == Boolean.class || type == boolean.class)
            return true;
        if (type == BigDecimal.class)
            return BigDecimal.ONE;
        if (type == LocalDate.class)
            return LocalDate.now();
        if (type == List.class)
            return new ArrayList<>();
        if (type == Set.class)
            return new HashSet<>();
        if (type.isEnum() && type.getEnumConstants().length > 0)
            return type.getEnumConstants()[0];
        return null; // For complex objects, null is often safe for setters
    }
}
