package org.protobeans.async.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.async.config.AsyncConfig;
import org.protobeans.async.config.AsyncPostProcessorConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({AsyncPostProcessorConfig.class, AsyncConfig.class})
@Configuration
public @interface EnableAsync {
    int corePoolSize() default 0;
    
    int maxPoolSize() default Integer.MAX_VALUE;
    
    boolean interruptOnClose() default false;
}
