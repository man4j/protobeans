package org.protobeans.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.mvc.config.MvcConfig;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MvcConfig.class)
public @interface EnableMvc {
    String resourcesPath() default "static";
    
    String resourcesUrl() default "static";
    
    String sessionCookieName() default "";
    
    String cacheMessageSource() default "false";
}