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
    
    int concurrency() default -1;
}
