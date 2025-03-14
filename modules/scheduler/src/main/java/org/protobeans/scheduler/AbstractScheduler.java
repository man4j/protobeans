package org.protobeans.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public abstract class AbstractScheduler<T extends ProcessableItem> {
    private static Logger log = LoggerFactory.getLogger(AbstractScheduler.class);

    ExecutorService executor = Executors.newCachedThreadPool();

    public int getConcurrency() {
        return 1;
    }
    
    public int getBackoffBaseDelaySeconds() {
        return 10;
    }
    
    public int getBackoffMaxDelaySeconds() {
        return 60;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < getConcurrency(); i++) {
            executor.execute(() -> {
                int sleep = 0;

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(sleep);
                        sleep = process();
                    } catch (@SuppressWarnings("unused") InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        log.error("", e);
                        sleep = 1_000;
                    }
                }
            });
        }
    }

    @PreDestroy
    public void stop() {
        try {
            executor.shutdownNow();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int process() {
        int processedItems = 0;

        for (var item : getItems()) {
            boolean needUpdate = false;

            try {
                needUpdate = processItem(item);
                item.setProcessTryCount(0);
                item.setErrorMessage(null);
                processedItems++;
            } catch (Exception e) {
                log.error("Failed to process object " + item, e);
                item.setNextProcessedTime(LocalDateTime.now().plusSeconds(calculateExponentialBackoff(item.getProcessTryCount())));
                item.setErrorMessage(e.getMessage());
                item.setProcessTryCount(item.getProcessTryCount() + 1);
                needUpdate = true;
            } finally {
                if (needUpdate) {
                    updateItem(item);
                }
            }
        }

        return processedItems == 0 ? 3_000 : 0;
    }
    
    private int calculateExponentialBackoff(int retryCount) {
        int delay = (int) (getBackoffBaseDelaySeconds() * Math.pow(2, retryCount - 1));
        return Math.min(delay, getBackoffMaxDelaySeconds());
    }

    public abstract List<T> getItems();

    public abstract boolean processItem(T item);

    public abstract void updateItem(T item);
}
