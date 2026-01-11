package org.protobeans.kafka.backoff;

import org.springframework.util.backoff.ExponentialBackOff;

public class ImprovedExponentialBackOff extends ExponentialBackOff {
    public ImprovedExponentialBackOff(long maxInterval, int maxAttempts) {
        setMaxInterval(maxInterval);
        setMaxAttempts(maxAttempts);
    }
    
    public ImprovedExponentialBackOff(long maxInterval) {
        setMaxInterval(maxInterval);
    }
}
