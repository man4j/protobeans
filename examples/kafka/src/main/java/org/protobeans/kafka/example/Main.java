package org.protobeans.kafka.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.protobeans.core.EntryPoint;
import org.protobeans.kafka.annotation.EnableKafkaMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@EnableKafkaMessaging(brokerList = "127.0.0.1:9092")
public class Main {
    @Autowired KafkaTemplate<String, String> kafkaTemplate;

    @EventListener(ContextRefreshedEvent.class)
    void start() throws InterruptedException, ExecutionException {
        while (!Thread.interrupted()) {
            kafkaTemplate.send("topic1", UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))).get();

            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(id = "group1", topics = "topic1")
    public void topicListener(ConsumerRecords<String, String> records) {
        for (var record : records) {
            System.out.println("Thread: " + Thread.currentThread().getName() 
                             + ", Partition: " + record.partition() 
                             + ", Value: " + record.value());

            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Bean
    public NewTopic mytopic() {
        return TopicBuilder.name("topic1").partitions(6).replicas(3).build();
    }

    public static void main(String[] args) {
        EntryPoint.run(Main.class);
    }
}