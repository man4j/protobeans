package org.protobeans.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.kafka.annotation.EnableKafkaMessaging;
import org.protobeans.kafka.listener.DeadLetterPublishingErrorHandler;
import org.protobeans.kafka.listener.SimpleRetryErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import lombok.extern.slf4j.Slf4j;


@Configuration
@InjectFrom(EnableKafkaMessaging.class)
@EnableKafka
@Slf4j
public class KafkaMessagingConfig {
    private String brokerList;

    private int concurrency;

    private String autoOffsetReset;

    private String maxPollRecords;
    
    private String maxPollIntervalMs;
    
    private String groupId;
    
    private boolean enableDeadLetterTopic;
    
    private int idleBetweenPolls;
    
    private int dltIdleBetweenPolls;
    
    @Autowired ApplicationContext ctx;
    
    /**
     * Основной контейнер
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        
        if (enableDeadLetterTopic) {
            factory.setCommonErrorHandler(new DeadLetterPublishingErrorHandler(kafkaTemplate()));
        } else {
            factory.setCommonErrorHandler(new SimpleRetryErrorHandler());
        }
        
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency == -1 ? Runtime.getRuntime().availableProcessors() : concurrency);
        factory.getContainerProperties().setLogContainerConfig(true);
        factory.getContainerProperties().setIdleBetweenPolls(idleBetweenPolls);

        return factory;
    }
    
    /**
     * Контейнер который разгребает DLT топики
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaDeadLeterListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        
        factory.setCommonErrorHandler(new SimpleRetryErrorHandler());
        factory.setBatchListener(true);
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency == -1 ? Runtime.getRuntime().availableProcessors() : concurrency);
        factory.getContainerProperties().setLogContainerConfig(true);
        factory.getContainerProperties().setIdleBetweenPolls(dltIdleBetweenPolls);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        
        return new DefaultKafkaProducerFactory<>(props);
    }
    
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);

        KafkaAdmin admin = new KafkaAdmin(configs);
        admin.setApplicationContext(ctx);
        return admin;
    }
}