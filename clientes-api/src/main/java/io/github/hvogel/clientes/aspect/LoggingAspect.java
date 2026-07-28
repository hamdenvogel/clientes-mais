package io.github.hvogel.clientes.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);
	
	@Around("execution(* io.github.hvogel.clientes.rest.AuthController.*(..)))")
    public Object logMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        final StopWatch stopWatch = new StopWatch();

        //calculate method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        //Log method execution time
        LOGGER.info("Execução do tempo: {}.{} :: {} ms",
                methodSignature.getDeclaringType().getSimpleName(),
                methodSignature.getName(),
                stopWatch.getTotalTimeMillis());

        return result;
    }
	
}
