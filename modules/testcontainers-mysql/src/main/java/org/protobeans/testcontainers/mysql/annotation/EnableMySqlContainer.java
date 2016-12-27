package org.protobeans.testcontainers.mysql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.testcontainers.mysql.listener.MySqlContainerListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestExecutionListeners(value = MySqlContainerListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface EnableMySqlContainer {
    String user();
    
    String password();
    
    String exposeUrlAs();
    
    String exposeUserAs();
    
    String exposePasswordAs();
}
