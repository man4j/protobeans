package org.protobeans.webservices.config;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.ws.transport.http.support.AbstractAnnotationConfigMessageDispatcherServletInitializer;

public class WsServletInitializer extends AbstractAnnotationConfigMessageDispatcherServletInitializer {
    public static ApplicationContext rootApplicationContext;
    
    public static String[] mappings;
    
    public static Class<?>[] configClasses;
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return configClasses;
    }

    @Override
    public boolean isTransformWsdlLocations() {
        return true;
    }
    
    @Override
    protected String[] getServletMappings() {
        return mappings;
    }
    
    @SuppressWarnings("resource")
    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        if (servletContext.getAttribute("rootAppCtx") == null) {
            AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
            
            ctx.setParent(rootApplicationContext);
    
            servletContext.addListener(new ContextLoaderListener(ctx));
            
            servletContext.setAttribute("rootAppCtx", ctx);
        }
    }
}
