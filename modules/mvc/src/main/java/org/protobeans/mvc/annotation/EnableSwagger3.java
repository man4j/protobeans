package org.protobeans.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocNativeConfiguration;
import org.springdoc.core.configuration.SpringDocPageableConfiguration;
import org.springdoc.core.configuration.SpringDocSecurityConfiguration;
import org.springdoc.core.configuration.SpringDocSortConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({//common jar
         SpringDocConfiguration.class,
         SpringDocConfigProperties.class,
         SpringDocNativeConfiguration.class,
         SpringDocPageableConfiguration.class,
         SpringDocSortConfiguration.class,
         SpringDocSecurityConfiguration.class,
    
         //api jar
         SpringDocWebMvcConfiguration.class,
         MultipleOpenApiSupportConfiguration.class,
         
         //ui jar
         SwaggerConfig.class,
         SwaggerUiConfigParameters.class,
         SwaggerUiConfigProperties.class,
         SwaggerUiOAuthProperties.class
         })
@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:swagger.properties")
public @interface EnableSwagger3 {
    //empty
}