package org.protobeans.freemarker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.freemarker.config.FreeMarkerConfig;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(FreeMarkerConfig.class)
public @interface EnableFreeMarker {
    String devMode();
}
