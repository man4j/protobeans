package org.protobeans.testcontainers.selenium.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableSeleniumContainer {
    int webPort() default 8080;

    String dockerHostIp() default "172.17.0.1";
    
    String dockerHostSshUser() default "root";
    
    String dockerHostSshKeyPath() default "";
    
    String exposeWebUrlAs() default "webUrl";
}
