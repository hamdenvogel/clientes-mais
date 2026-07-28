package io.github.hvogel.clientes.aspect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @InjectMocks
    private LoggingAspect loggingAspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private MethodSignature methodSignature;

    @Test
    void testLogMethodExecutionTime() throws Throwable {
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn(LoggingAspectTest.class);
        when(methodSignature.getName()).thenReturn("testMethod");
        when(proceedingJoinPoint.proceed()).thenReturn("Result");

        Object result = loggingAspect.logMethodExecutionTime(proceedingJoinPoint);

        assertEquals("Result", result);
        verify(proceedingJoinPoint).proceed();
    }
}
