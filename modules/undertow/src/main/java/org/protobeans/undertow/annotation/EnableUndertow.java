package org.protobeans.undertow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EventListener;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.protobeans.undertow.config.UndertowConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(UndertowConfig.class)
@Configuration
public @interface EnableUndertow {
    String host() default "0.0.0.0";
    
    String port() default "8080";
    
    Initializer[] initializers() default {};
    
    Initializer[] userInitializers() default {};
    
    Class<? extends EventListener>[] listeners() default {};
    
    String resourcesPath() default "undefined";
    
    String welcomePage() default "index.html";
    
    String errorPage() default "";
    
    int sessionTimeout() default 30000;
    
    int proxyConnectionsCount() default 20;
    
    String workerThreads() default "-1";
    
    String ioThreads() default "-1";
    
    String[] ignoreProxyPathPrefix() default {};

    String proxyBackend() default "";
}