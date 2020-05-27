package org.protobeans.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.MultipleOpenApiSupportConfiguration;
import org.springdoc.webmvc.core.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({SpringDocConfiguration.class,
         SpringDocWebMvcConfiguration.class,
         
         SpringDocConfigProperties.class,
         SwaggerUiConfigProperties.class,
         SwaggerUiOAuthProperties.class,
         
         MultipleOpenApiSupportConfiguration.class,
         SwaggerConfig.class})
@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:swagger.properties")
public @interface EnableSwagger3 {
    

}