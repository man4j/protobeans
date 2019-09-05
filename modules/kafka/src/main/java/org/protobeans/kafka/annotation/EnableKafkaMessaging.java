package org.protobeans.kafka.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.kafka.config.KafkaMessagingConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(KafkaMessagingConfig.class)
@Configuration
public @interface EnableKafkaMessaging {
    String brokerList();
    
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
    String transactionalIdPrefix();

    int concurrency() default -1;
    
    String autoOffsetReset() default "earliest";
    
    String maxPollRecords() default "500";
}
