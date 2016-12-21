package org.protobeans.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EntryPoint {
    @SuppressWarnings({ "resource" })
    public static void run(Class<?>... annotatedClasses) {
        new AnnotationConfigApplicationContext(annotatedClasses).registerShutdownHook();
    }
}
