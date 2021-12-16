package org.protobeans.faces.config.annotation;

import org.protobeans.faces.config.ProtobeansFacesConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ProtobeansFacesConfig.class)
@Configuration
public @interface EnableFaces {

}