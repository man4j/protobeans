package org.protobeans.undertow;

import org.protobeans.undertow.config.UndertowConfig;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MvcEntryPoint {
    public static AnnotationConfigWebApplicationContext run(Class<?> mainClass) {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

        ctx.register(UndertowConfig.class, mainClass);
        ctx.refresh();
        ctx.registerShutdownHook();

        return ctx;
    }
}
