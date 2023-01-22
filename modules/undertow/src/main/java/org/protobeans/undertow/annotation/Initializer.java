package org.protobeans.undertow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.servlet.ServletContainerInitializer;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Initializer {
    Class<? extends ServletContainerInitializer> initializer();
    
    Class<?>[] handleTypes() default {};
}
