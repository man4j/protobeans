package org.protobeans.mvc;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MvcEntryPoint {
    public static AnnotationConfigWebApplicationContext run(Class<?>... annotatedClasses) {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        
        ctx.register(annotatedClasses);
        
        ctx.refresh();
        
        ctx.registerShutdownHook();
        
        return ctx;
    }
}
