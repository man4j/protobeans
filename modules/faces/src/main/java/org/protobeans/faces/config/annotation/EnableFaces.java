package org.protobeans.faces.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.weld.environment.servlet.Listener;
import org.omnifaces.ApplicationInitializer;
import org.protobeans.faces.config.ContextParamsInitializer;
import org.protobeans.faces.config.ProtobeansFacesConfig;
import org.protobeans.undertow.annotation.EnableUndertow;
import org.protobeans.undertow.annotation.Initializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import com.sun.faces.config.FacesInitializer;
import com.sun.faces.config.FacesInitializer2;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ProtobeansFacesConfig.class)
@Configuration
@EnableUndertow(resourcesPath = "META-INF/resources", 
                initializers = {@Initializer(initializer = ContextParamsInitializer.class),
                                @Initializer(initializer = FacesInitializer.class),
                                @Initializer(initializer = FacesInitializer2.class),
                                @Initializer(initializer = ApplicationInitializer.class)},
                listeners = {Listener.class})
public @interface EnableFaces {
    @AliasFor(annotation = EnableUndertow.class, attribute = "port")
    String port() default "8080";
    
    @AliasFor(annotation = EnableUndertow.class, attribute = "sessionTimeout")
    int sessionTimeout() default 30000;
    
    @AliasFor(annotation = EnableUndertow.class, attribute = "userInitializers")
    Initializer[] userInitializers() default {};
}