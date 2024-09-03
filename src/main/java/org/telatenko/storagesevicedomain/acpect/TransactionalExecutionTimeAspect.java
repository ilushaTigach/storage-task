package org.telatenko.storagesevicedomain.acpect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class TransactionalExecutionTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(TransactionalExecutionTimeAspect.class);

    @Around("@annotation(org.telatenko.storagesevicedomain.annotation.MeasureTransactionalExecutionTime) " +
            "&& @annotation(org.springframework.transaction.annotation.Transactional)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}
