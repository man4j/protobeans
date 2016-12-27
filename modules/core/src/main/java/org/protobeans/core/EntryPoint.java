package org.protobeans.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EntryPoint {
    public static AnnotationConfigApplicationContext run(Class<?>... annotatedClasses) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(annotatedClasses);
        
        ctx.registerShutdownHook();
        
        return ctx;
    }
}
