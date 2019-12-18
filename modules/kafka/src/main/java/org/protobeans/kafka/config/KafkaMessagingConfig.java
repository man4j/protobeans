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
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.ContainerStoppingBatchErrorHandler;
import org.springframework.kafka.listener.ContainerStoppingErrorHandler;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/***
 * 
   KafkaProducer producer = createKafkaProducer(
     “bootstrap.servers”, “localhost:9092”,
     “transactional.id”, “my-transactional-id”);

   producer.initTransactions();

   KafkaConsumer consumer = createKafkaConsumer(
     “bootstrap.servers”, “localhost:9092”,
     “group.id”, “my-group-id”,
     "isolation.level", "read_committed");

   consumer.subscribe(singleton(“inputTopic”));

   while (true) {
     ConsumerRecords records = consumer.poll(Long.MAX_VALUE);
     producer.beginTransaction();
     for (ConsumerRecord record : records) {
       producer.send(producerRecord(“outputTopic”, record));
     }
     producer.sendOffsetsToTransaction(currentOffsets(consumer), group);  
     producer.commitTransaction();
   }
 *
 */
@Configuration
@InjectFrom(EnableKafkaMessaging.class)
@EnableKafka
@EnableTransactionManagement(proxyTargetClass = true)
public class KafkaMessagingConfig {
    private String brokerList;
    
    private String transactionalIdPrefix;

    private int concurrency;
    
    private String autoOffsetReset;
    
    private String maxPollRecords;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency == -1 ? Runtime.getRuntime().availableProcessors() : concurrency);
        factory.setBatchListener(true);
        factory.setErrorHandler(new ContainerStoppingErrorHandler());
        factory.setBatchErrorHandler(new ContainerStoppingBatchErrorHandler());
        
        factory.getContainerProperties().setAckMode(AckMode.BATCH);
        factory.getContainerProperties().setTransactionManager(kafkaTransactionManager());
        factory.getContainerProperties().setSubBatchPerPartition(true);
        
        return factory;
    }
        
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }
    
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "DEFAULT.GROUP");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);// By default, offsets are configured to be automatically committed during the consumer’s poll() call at a periodic interval
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        
        return props;
    }
    
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        DefaultKafkaProducerFactory<String, String> defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        
        /**
         * Per Producer. This property is mapped to the transactional.id directly like this:

           configs.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,
                    this.transactionIdPrefix + this.transactionIdSuffix.getAndIncrement());
                    
           You really can stick with the same value here. It definitely must be different for 
           several producer-based applications in parallel, but that's fine to reuse the same transaction.id.prefix over restart.
           
           If you are running multiple instances of the same app concurrently, each app instance needs a unique prefix. Otherwise 
           you'll get transaction.id collisions. You don't need a different value for non-concurrent executions of the same application.
           
           Using :
           props.put("transaction.id.prefix", UUID.randomUUID());
           Is not a good idea since if the app crash it will generate a new one (on restart) instead of using the last prefix to resume ongoing transactions                    
         */
        defaultKafkaProducerFactory.setTransactionIdPrefix(transactionalIdPrefix);
        
        return defaultKafkaProducerFactory;
    }
    
    @Bean
    public KafkaTransactionManager<String, String> kafkaTransactionManager() {
        KafkaTransactionManager<String, String> kafkaTransactionManager = new KafkaTransactionManager<>(producerFactory());
        kafkaTransactionManager.setTransactionIdPrefix(transactionalIdPrefix);
        
        return kafkaTransactionManager;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        
        return props;
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
