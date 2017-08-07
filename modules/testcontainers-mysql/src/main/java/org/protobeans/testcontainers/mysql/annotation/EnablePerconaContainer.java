package org.protobeans.testcontainers.mysql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnablePerconaContainer {
    String imageTag() default "latest";
    
    String schema();

    String rootPassword();
    
    String exposeSchemaAs();
    
    String exposeUrlAs();
    
    String exposePasswordAs();
    
    boolean skipInit() default false;
}