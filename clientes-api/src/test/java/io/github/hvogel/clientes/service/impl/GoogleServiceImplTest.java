package io.github.hvogel.clientes.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import io.github.hvogel.clientes.exception.RegraNegocioException;
import io.github.hvogel.clientes.model.entity.Captcha;
import io.github.hvogel.clientes.rest.dto.GoogleRecaptchaDTO;
import io.github.hvogel.clientes.service.ReCaptchaAttemptService;
import io.github.hvogel.clientes.service.ValidadorService;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class GoogleServiceImplTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private ReCaptchaAttemptService reCaptchaAttemptService;

    @Mock
    private Captcha captcha;

    @Mock
    private ValidadorService validadorService;

    @Mock
    private RestTemplate restTemplate;

    // We can't use @InjectMocks with Spy effectively on constructor injection if we
    // want to spy the service itself
    // AND mock its dependencies easily unless we handle it manually.
    private GoogleServiceImpl service;

    @BeforeEach
    void setUp() {
        // Validation service void return default is fine
        when(request.getHeader("X-Forwarded-For")).thenReturn("127.0.0.1");
        service = new GoogleServiceImpl(request, reCaptchaAttemptService, captcha, validadorService);
        service = org.mockito.Mockito.spy(service);
        doReturn(restTemplate).when(service).createRestTemplate();

        when(captcha.getSecret()).thenReturn("secret");
    }

    @Test
    void testValidarToken_Success() {
        GoogleRecaptchaDTO responseDTO = new GoogleRecaptchaDTO();
        responseDTO.setSuccess(true);

        when(reCaptchaAttemptService.isBlocked(anyString())).thenReturn(false);
        when(restTemplate.getForObject(any(URI.class), eq(GoogleRecaptchaDTO.class))).thenReturn(responseDTO);

        GoogleRecaptchaDTO result = service.validarToken("valid-token");
        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        verify(reCaptchaAttemptService).reCaptchaSucceeded(anyString());
    }

    @Test
    void testValidarToken_Blocked() {
        when(reCaptchaAttemptService.isBlocked(anyString())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> service.validarToken("token"));
    }

    @Test
    void testValidarToken_GoogleFailure() {
        GoogleRecaptchaDTO responseDTO = new GoogleRecaptchaDTO();
        responseDTO.setSuccess(false);
        // Set error codes to trigger reCaptchaFailed
        responseDTO.setErrorCodes(new GoogleRecaptchaDTO.ErrorCode[] { GoogleRecaptchaDTO.ErrorCode.INVALID_RESPONSE });

        when(reCaptchaAttemptService.isBlocked(anyString())).thenReturn(false);
        when(restTemplate.getForObject(any(URI.class), eq(GoogleRecaptchaDTO.class))).thenReturn(responseDTO);

        assertThrows(RegraNegocioException.class, () -> service.validarToken("token"));
        verify(reCaptchaAttemptService).reCaptchaFailed(anyString());
    }

    @Test
    void testValidarToken_ClientError() {
        GoogleRecaptchaDTO responseDTO = new GoogleRecaptchaDTO();
        responseDTO.setSuccess(false);
        // Simulate client error? The code checks hasClientError().
        // Setting an error code that is NOT a client error to test negative branch
        responseDTO.setErrorCodes(new GoogleRecaptchaDTO.ErrorCode[] { GoogleRecaptchaDTO.ErrorCode.INVALID_SECRET });

        when(reCaptchaAttemptService.isBlocked(anyString())).thenReturn(false);
        when(restTemplate.getForObject(any(URI.class), eq(GoogleRecaptchaDTO.class))).thenReturn(responseDTO);
        assertThrows(RegraNegocioException.class, () -> service.validarToken("token"));
        // hasClientError returns false for INVALID_SECRET, so reCaptchaFailed should
        // NOT be called
        verify(reCaptchaAttemptService, org.mockito.Mockito.never()).reCaptchaFailed(anyString());
    }

    @Test
    void testValidarToken_RestClientException() {
        when(restTemplate.getForObject(any(URI.class), eq(GoogleRecaptchaDTO.class)))
                .thenThrow(new RestClientException("Error"));

        assertThrows(RegraNegocioException.class, () -> service.validarToken("token"));
    }

    @Test
    void testValidarToken_NullResponse() {
        when(restTemplate.getForObject(any(URI.class), eq(GoogleRecaptchaDTO.class))).thenReturn(null);
        assertThrows(RegraNegocioException.class, () -> service.validarToken("token"));
    }

    @Test
    void testZerarTentativas() {
        service.zerarTentativasMalSucedidas();
        verify(reCaptchaAttemptService).reCaptchaDeleteAllEntriesInTheCache();
    }

    @Test
    void testValidarToken_ValidadorException() {
        org.mockito.Mockito.doThrow(new RegraNegocioException("Invalid token format"))
                .when(validadorService).validarTokenGoogle("bad-token");

        assertThrows(ResponseStatusException.class, () -> service.validarToken("bad-token"));
    }

    @Test
    void testObterClientIP_RemoteAddr() {
        // Use a fresh instance or adjust mock for this test
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.50");

        String ip = service.obterClientIP();
        assertEquals("192.168.1.50", ip);
    }

    @Test
    void testObterClientIP_XForwardedFor_Multi() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 10.0.0.2");

        String ip = service.obterClientIP();
        assertEquals("10.0.0.1", ip);
    }

    @Test
    void testObterInformacao() {
        when(reCaptchaAttemptService.reCapthaSizeOfCacheVersusTotalAttempts()).thenReturn("1/5");
        assertEquals("1/5", service.obterInformacaoNumeroDeTentativasDeAcessoAoCapctha());
    }

    @Test
    void testCreateRestTemplate() {
        // Invoke the protected method directly (accessible in same package)
        // Since 'service' is a spy with createRestTemplate stubbed in setUp,
        // we should better create a fresh instance or callRealMethod logic.
        // Easiest is to create a fresh raw instance to test the method logic itself.
        GoogleServiceImpl rawService = new GoogleServiceImpl(request, reCaptchaAttemptService, captcha,
                validadorService);
        RestTemplate rt = rawService.createRestTemplate();
        assertNotNull(rt);
        assertNotNull(rt.getRequestFactory());
        assertEquals(1, rt.getInterceptors().size());
    }

    @Test
    void testValidarCaptchaPreenchido_Success() {
        assertNotNull(service);
        service.validarCaptchaPreenchido("valid-captcha");
    }

    @Test
    void testValidarCaptchaPreenchido_Null() {
        assertThrows(RegraNegocioException.class, () -> service.validarCaptchaPreenchido(null));
    }

    @Test
    void testValidarCaptchaPreenchido_Empty() {
        assertThrows(RegraNegocioException.class, () -> service.validarCaptchaPreenchido(""));
    }
}
