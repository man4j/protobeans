package org.protobeans.undertow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.undertow.config.UndertowConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(UndertowConfig.class)
@Configuration
public @interface EnableUndertow {
    String host() default "0.0.0.0";
    
    String port() default "8080";
    
    String resourcesPath() default "undefined";
    
    String welcomePage() default "index.html";
    
    String errorPage() default "";
    
    int sessionTimeout() default 30000;
    
    int proxyConnectionsCount() default 20;
    
    String workerThreads() default "-1";
    
    String ioThreads() default "-1";
    
    String[] ignoreProxyPathPrefix() default {};

    String proxyBackend() default "";
    
    String uploadLocation() default "/tmp"; 
    
    long maxFileSize() default 20_971_520; 
    
    long maxRequestSize() default 20_971_520;
    
    int fileSizeThreshold() default 1_048_576;
}