package org.protobeans.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EntryPoint {
    public static AnnotationConfigApplicationContext run(Class<?>... annotatedClasses) {
        var ctx = new AnnotationConfigApplicationContext(annotatedClasses);
        ctx.registerShutdownHook();
        return ctx;
    }
}
