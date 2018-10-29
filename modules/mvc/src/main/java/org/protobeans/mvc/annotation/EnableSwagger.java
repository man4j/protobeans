package org.protobeans.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.mvc.config.SwaggerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.swagger.annotations.Info;
import io.swagger.annotations.Tag;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({SwaggerConfig.class})
@Configuration
public @interface EnableSwagger {
    Info info() default @Info(title = "", version = "");
    
    Tag[] tags() default @Tag(name = "default");
}