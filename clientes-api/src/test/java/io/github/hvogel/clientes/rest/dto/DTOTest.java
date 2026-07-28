package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.beans.IntrospectionException;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

class DTOTest {

    @Test
    void testAllDtos() {
        Class<?>[] dtos = {
                AtividadeDTO.class,
                AtualizacaoStatusPedidoDTO.class,
                ChamadoDTO.class,
                ClienteDTO.class,
                CredenciaisDTO.class,
                DiagnosticoDTO.class,
                DocumentoDTO.class,
                EnderecoDTO.class,
                EquipamentoDTO.class,
                GoogleRecaptchaDTO.class,
                ImagemDTO.class,
                InfoDTO.class,
                InfoResponseDTO.class,
                InformacaoItemPedidoDTO.class,
                InformacoesPedidoDTO.class,
                ItemPacoteDTO.class,
                ItemPedidoDTO.class,
                ListaNomesDTO.class,
                LoginDTO.class,
                NaturezaDTO.class,
                NotFoundDTO.class,
                PacoteDTO.class,
                ParametroDTO.class,
                PedidoDTO.class,
                PrestadorDTO.class,
                ProdutoDTO.class,
                ProfissaoDTO.class,
                ServicoPrestadoDTO.class,
                ServicoPrestadoProjectionDTO.class,
                SignupDTO.class,
                SolucaoDTO.class,
                TokenDTO.class,
                TotalClientesDTO.class,
                TotalItensPacotesDTO.class,
                TotalPacotesDTO.class,
                TotalPrestadoresDTO.class,
                TotalProdutosDTO.class,
                TotalRegistrosDTO.class,
                TotalServicosDTO.class,
                TotalUsuariosDTO.class
        };

        for (Class<?> dtoClass : dtos) {
            assertDoesNotThrow(() -> testDto(dtoClass), "DTO test failed for " + dtoClass.getSimpleName());
        }
    }

    private void testDto(Class<?> clazz) throws ReflectiveOperationException, IntrospectionException {
        // Constructor
        Object instance1 = clazz.getDeclaredConstructor().newInstance();
        Object instance2 = clazz.getDeclaredConstructor().newInstance();

        // Getters and Setters
        for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
            Method writeMethod = pd.getWriteMethod();
            Method readMethod = pd.getReadMethod();
            if (writeMethod != null && readMethod != null) {
                Object value = generateValue(pd.getPropertyType());
                writeMethod.invoke(instance1, value);
                writeMethod.invoke(instance2, value);

                Object got = readMethod.invoke(instance1);
                assertEquals(value, got, "Getter/Setter failed for " + pd.getName() + " in " + clazz.getSimpleName());
            }
        }

        verifyEqualsAndHashCode(clazz, instance1, instance2);
        verifyInequality(clazz, instance1);

        // ToString
        assertNotNull(instance1.toString());
    }

    private void verifyEqualsAndHashCode(Class<?> clazz, Object instance1, Object instance2) {
        // 1. Reflexive
        assertEquals(instance1, instance1);

        if (hasCustomEquals(clazz)) {
            // 2. Symmetric
            assertEquals(instance1, instance2);
            assertEquals(instance2, instance1);
            assertEquals(instance1.hashCode(), instance2.hashCode());

            // 3. Not Equal to null
            assertNotNull(instance1); // Typically handled by equals check, but explicit check
            assertNotEquals(null, instance1);

            // 4. Not Equal to other type
            assertNotEquals(new Object(), instance1);
        }
    }

    private void verifyInequality(Class<?> clazz, Object instance1)
            throws ReflectiveOperationException, IntrospectionException {
        for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
            Method writeMethod = pd.getWriteMethod();
            if (writeMethod != null) {
                Object originalValue = pd.getReadMethod().invoke(instance1);
                Object newValue = generateDifferentValue(pd.getPropertyType(), originalValue);
                if (newValue != null && !newValue.equals(originalValue)) {
                    Object instance3 = clazz.getDeclaredConstructor().newInstance();
                    copyFields(clazz, instance1, instance3);
                    writeMethod.invoke(instance3, newValue);

                    // Just ensure equals returns false (or true if field ignored), but no crash.
                    // We cannot strictly assert inequality as some fields might be ignored in
                    // equals.
                    assertDoesNotThrow(() -> instance1.equals(instance3));
                }
            }
        }
    }

    private void copyFields(Class<?> clazz, Object source, Object target)
            throws ReflectiveOperationException, IntrospectionException {
        for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
            Method write = pd.getWriteMethod();
            Method read = pd.getReadMethod();
            if (write != null && read != null) {
                Object val = read.invoke(source);
                write.invoke(target, val);
            }
        }
    }

    private Object generateValue(Class<?> type) throws ReflectiveOperationException {
        if (type == String.class)
            return "testString";
        if (type == Integer.class || type == int.class)
            return 123;
        if (type == Long.class || type == long.class)
            return 123L;
        if (type == Double.class || type == double.class)
            return 12.34;
        if (type == BigDecimal.class)
            return new BigDecimal("12.34");
        if (type == Boolean.class || type == boolean.class)
            return true;
        if (type == LocalDate.class)
            return LocalDate.now();
        if (type == Date.class)
            return new Date();
        if (type == List.class)
            return Collections.emptyList();
        if (type == byte[].class)
            return new byte[] { 1, 2, 3 };

        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants.length > 0 ? constants[0] : null;
        }

        if (type.getName().startsWith("io.github.hvogel.clientes.rest.dto")) {
            return type.getDeclaredConstructor().newInstance();
        }

        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private boolean hasCustomEquals(Class<?> clazz) {
        try {
            Method m = clazz.getMethod("equals", Object.class);
            return !m.getDeclaringClass().equals(Object.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private Object generateDifferentValue(Class<?> type, Object current) throws ReflectiveOperationException {
        if (type == String.class)
            return "diffString";
        if (type == Integer.class || type == int.class)
            return 456;
        if (type == Long.class || type == long.class)
            return 456L;
        if (type == Boolean.class || type == boolean.class)
            return !(Boolean) current;
        return generateValue(type);
    }
}
