package org.protobeans.mvc;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MvcEntryPoint {
    public static ApplicationContext run(Class<?>... annotatedClasses) {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        
        ctx.register(annotatedClasses);
        
        ctx.refresh();
        
        ctx.registerShutdownHook();
        
        return ctx;
    }
}
