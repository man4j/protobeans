package org.protobeans.cockroachdb.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class RetryableTransactionTemplate implements TransactionOperations {
    private static Logger log = LoggerFactory.getLogger(RetryableTransactionTemplate.class);

    private TransactionTemplate crdbTransactionTemplate;
    
    public RetryableTransactionTemplate(TransactionTemplate crdbTransactionTemplate) {
        this.crdbTransactionTemplate = crdbTransactionTemplate;
    }

    @Override
    public <T> T execute(TransactionCallback<T> action) throws TransactionException {
        do {
            try {
                return crdbTransactionTemplate.execute(t -> {
                    return action.doInTransaction(t);
                });
            } catch (CannotAcquireLockException e) {
                log.warn("Transaction conflict detected", e);
                continue;
            } catch (Exception e) {
                log.error("Unknown exception", e);
                throw e;
            }
        } while (true);
    }
}