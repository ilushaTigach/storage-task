package org.telatenko.storagesevicedomain.acpect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    long executionTime = System.currentTimeMillis() - start;
                    logger.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);
                }
            });
        } else {
            long executionTime = System.currentTimeMillis() - start;
            logger.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);
        }
        return proceed;
    }
}
