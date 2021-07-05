package org.protobeans.cockroachdb.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class RetryableTransactionTemplate implements TransactionOperations {
    private static Logger log = LoggerFactory.getLogger(RetryableTransactionTemplate.class);
    
    @Autowired
    private TransactionTemplate crdbTransactionTemplate;
    
    @Override
    public <T> T execute(TransactionCallback<T> action) throws TransactionException {
        do {
            try {
                return crdbTransactionTemplate.execute(t -> {
                    return action.doInTransaction(t);
                });
            } catch (@SuppressWarnings("unused") CannotAcquireLockException e) {
                log.warn("Transaction conflict detected");
                continue;
            }
        } while (true);
    }
}
