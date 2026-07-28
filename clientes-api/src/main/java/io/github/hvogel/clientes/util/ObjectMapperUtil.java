package io.github.hvogel.clientes.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.ValidationException;
import java.io.IOException;

public class ObjectMapperUtil {
	private ObjectMapperUtil() {
        throw new ValidationException("Classe Utilitária não pode ser instanciada.");
    }

    public static <T> T getObject(String jsonStr, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = getObjectMapper();
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new ValidationException("Erro ao desserializacao objeto javascript (json) - " + e.getMessage());
        }
    }

    public static String getJsonOfObject(Object object) {
        try {
            ObjectMapper objectMapper = getObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ValidationException("Erro ao serializar objeto java - " + e.getMessage());
        }
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
