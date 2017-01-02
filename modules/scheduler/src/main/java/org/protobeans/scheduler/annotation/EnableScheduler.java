package org.protobeans.scheduler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.scheduler.config.SchedulerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SchedulerConfig.class)
@Configuration
public @interface EnableScheduler {
    int poolSize() default 1;
    
    boolean interruptOnClose() default false;
}
