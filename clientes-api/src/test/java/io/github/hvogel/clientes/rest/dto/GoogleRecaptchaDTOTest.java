package io.github.hvogel.clientes.rest.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.hvogel.clientes.rest.dto.GoogleRecaptchaDTO.ErrorCode;

class GoogleRecaptchaDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();

        // Act
        dto.setSuccess(true);
        dto.setChallengeTs("2025-12-13T10:00:00Z");
        dto.setHostname("example.com");
        dto.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_RESPONSE });

        // Assert
        assertTrue(dto.isSuccess());
        assertEquals("2025-12-13T10:00:00Z", dto.getChallengeTs());
        assertEquals("example.com", dto.getHostname());
        assertNotNull(dto.getErrorCodes());
        assertEquals(1, dto.getErrorCodes().length);
        assertEquals(ErrorCode.MISSING_RESPONSE, dto.getErrorCodes()[0]);
    }

    @Test
    void testHasClientErrorWithInvalidResponse() {
        // Arrange
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();
        dto.setErrorCodes(new ErrorCode[] { ErrorCode.INVALID_RESPONSE });

        // Act & Assert
        assertTrue(dto.hasClientError());
    }

    @Test
    void testHasClientErrorWithMissingResponse() {
        // Arrange
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();
        dto.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_RESPONSE });

        // Act & Assert
        assertTrue(dto.hasClientError());
    }

    @Test
    void testHasClientErrorWithTimeoutOrDuplicate() {
        // Arrange
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();
        dto.setErrorCodes(new ErrorCode[] { ErrorCode.TIMEOUT_OR_DUPLICATE });

        // Act & Assert
        assertTrue(dto.hasClientError());
    }

    @Test
    void testHasClientErrorWithServerError() {
        // Arrange
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();
        dto.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_SECRET });

        // Act & Assert
        assertFalse(dto.hasClientError());
    }

    @Test
    void testHasClientErrorWithNullErrorCodes() {
        // Arrange
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();
        dto.setErrorCodes(null);

        // Act & Assert
        assertFalse(dto.hasClientError());
    }

    @Test
    void testHasClientErrorWithMultipleErrors() {
        // Arrange
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();
        dto.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_SECRET, ErrorCode.INVALID_RESPONSE });

        // Act & Assert
        assertTrue(dto.hasClientError());
    }

    @Test
    void testEquals() {
        // Arrange
        GoogleRecaptchaDTO dto1 = new GoogleRecaptchaDTO();
        dto1.setSuccess(true);
        dto1.setChallengeTs("2025-12-13T10:00:00Z");
        dto1.setHostname("example.com");
        dto1.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_RESPONSE });

        GoogleRecaptchaDTO dto2 = new GoogleRecaptchaDTO();
        dto2.setSuccess(true);
        dto2.setChallengeTs("2025-12-13T10:00:00Z");
        dto2.setHostname("example.com");
        dto2.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_RESPONSE });

        GoogleRecaptchaDTO dto3 = new GoogleRecaptchaDTO();
        dto3.setSuccess(false);

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1, dto1);
        assertNotEquals(dto1, dto3);
        assertNotEquals(null, dto1);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void testHashCode() {
        // Arrange
        GoogleRecaptchaDTO dto1 = new GoogleRecaptchaDTO();
        dto1.setSuccess(true);
        dto1.setChallengeTs("2025-12-13T10:00:00Z");
        dto1.setHostname("example.com");
        dto1.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_RESPONSE });

        GoogleRecaptchaDTO dto2 = new GoogleRecaptchaDTO();
        dto2.setSuccess(true);
        dto2.setChallengeTs("2025-12-13T10:00:00Z");
        dto2.setHostname("example.com");
        dto2.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_RESPONSE });

        // Assert
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        GoogleRecaptchaDTO dto = new GoogleRecaptchaDTO();
        dto.setSuccess(true);
        dto.setChallengeTs("2025-12-13T10:00:00Z");
        dto.setHostname("example.com");
        dto.setErrorCodes(new ErrorCode[] { ErrorCode.MISSING_RESPONSE });

        // Act
        String result = dto.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("success=true"));
        assertTrue(result.contains("challengeTs=2025-12-13T10:00:00Z"));
        assertTrue(result.contains("hostname=example.com"));
        assertTrue(result.contains("MISSING_RESPONSE"));
    }

    @Test
    void testErrorCodeForValue() {
        // Act & Assert
        assertEquals(ErrorCode.MISSING_SECRET, ErrorCode.forValue("missing-input-secret"));
        assertEquals(ErrorCode.INVALID_SECRET, ErrorCode.forValue("invalid-input-secret"));
        assertEquals(ErrorCode.MISSING_RESPONSE, ErrorCode.forValue("missing-input-response"));
        assertEquals(ErrorCode.INVALID_RESPONSE, ErrorCode.forValue("invalid-input-response"));
        assertEquals(ErrorCode.TIMEOUT_OR_DUPLICATE, ErrorCode.forValue("timeout-or-duplicate"));
    }

    @Test
    void testErrorCodeForValueCaseInsensitive() {
        // Act & Assert
        assertEquals(ErrorCode.MISSING_SECRET, ErrorCode.forValue("MISSING-INPUT-SECRET"));
        assertEquals(ErrorCode.INVALID_SECRET, ErrorCode.forValue("Invalid-Input-Secret"));
    }
}
