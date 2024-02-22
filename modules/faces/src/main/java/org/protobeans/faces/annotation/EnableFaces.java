package org.protobeans.faces.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.faces.config.ProtobeansFacesConfig;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ProtobeansFacesConfig.class)
@Configuration
@EnableUndertow(resourcesPath = "META-INF/resources")
public @interface EnableFaces {
    @AliasFor(annotation = EnableUndertow.class, attribute = "port")
    String port() default "8080";

    @AliasFor(annotation = EnableUndertow.class, attribute = "sessionTimeout")
    int sessionTimeout() default 30000;

    @AliasFor(annotation = EnableUndertow.class, attribute = "resourcesPath")
    String resourcesPath() default "META-INF/resources";
}
