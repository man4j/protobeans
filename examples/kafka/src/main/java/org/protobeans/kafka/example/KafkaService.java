package org.protobeans.kafka.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Transactional(rollbackFor = Exception.class, transactionManager = "kafkaTransactionManager")
    public void send(String topic, String key, String value) {
        kafkaTemplate.send(topic, key, value);
            
        System.out.println("Sent: " + value);
    }
}
