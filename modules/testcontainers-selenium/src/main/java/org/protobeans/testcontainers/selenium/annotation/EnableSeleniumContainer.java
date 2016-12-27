package org.protobeans.testcontainers.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.testcontainers.selenium.listener.SeleniumContainerListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestExecutionListeners(value = SeleniumContainerListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface EnableSeleniumContainer {
    int webPort() default 8080;

    String dockerHostIp() default "172.17.0.1";
    
    String dockerHostSshUser() default "root";
    
    String dockerHostSshKeyPath() default "";
    
    String exposeWebUrlAs() default "webUrl";
}
