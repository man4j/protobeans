package org.protobeans.kafka.listener;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.protobeans.kafka.backoff.ImprovedExponentialBackOff;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RetryListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadLetterPublishingErrorHandler extends DefaultErrorHandler {
    public DeadLetterPublishingErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        super(new DeadLetterPublishingRecoverer(kafkaTemplate), 
              new ImprovedExponentialBackOff(TimeUnit.SECONDS.toMillis(15), 3));

        setClassifications(Map.of(), true);
        setRetryListeners(new RetryListener() {
            @Override
            public void failedDelivery(ConsumerRecord<?, ?> rec, Exception ex, int attemp) {
                log.error("Message delivery failed", ex);
            }
            
            @Override
            public void failedDelivery(ConsumerRecords<?, ?> records, Exception ex, int attemp) {
                log.error("Message delivery failed", ex);
            }
        });
    }
}

