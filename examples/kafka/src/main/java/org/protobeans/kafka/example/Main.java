package org.protobeans.kafka.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.apache.kafka.clients.admin.NewTopic;
import org.protobeans.core.EntryPoint;
import org.protobeans.kafka.annotation.EnableKafkaMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafkaMessaging(brokerList = "91.242.38.69:9092")
@ComponentScan(basePackageClasses=KafkaService.class)
public class Main {
    @Autowired
    private KafkaService kafkaService;
    
    @EventListener(ContextRefreshedEvent.class)
    void start() {
        while (!Thread.interrupted()) {
            kafkaService.send("topic1", UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @KafkaListener(id = "group1", topics = "topic1")
    public void topicListener(List<String> records) {
        for (String value : records) {
            System.out.println("Receive: " + value);
            
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Bean
    public NewTopic mytopic() {
        return new NewTopic("topic1", 6, (short) 3);
    }
    
    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}
