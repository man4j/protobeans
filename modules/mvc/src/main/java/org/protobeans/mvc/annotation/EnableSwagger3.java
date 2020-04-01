package org.protobeans.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.mvc.config.Swagger3Config;
import org.springdoc.core.MultipleOpenApiSupportConfiguration;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SpringDocWebMvcConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({Swagger3Config.class,
         MultipleOpenApiSupportConfiguration.class,
         SpringDocWebMvcConfiguration.class,
         SpringDocConfiguration.class,
         SpringDocConfigProperties.class,
         SwaggerUiConfigProperties.class,
         SwaggerUiOAuthProperties.class})
public @interface EnableSwagger3 {

}