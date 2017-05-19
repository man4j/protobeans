package org.protobeans.testcontainers.mysql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableMySqlContainer {
    String user();
    
    String password();
    
    String exposeUrlAs();
    
    String exposeSchemaAs();
    
    String exposeUserAs();
    
    String exposePasswordAs();
}
