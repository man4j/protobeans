package org.protobeans.webservices.config;

import javax.servlet.ServletContext;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.ws.transport.http.support.AbstractAnnotationConfigMessageDispatcherServletInitializer;

public class WsServletInitializer extends AbstractAnnotationConfigMessageDispatcherServletInitializer {
    public static ConfigurableWebApplicationContext rootApplicationContext;
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    public boolean isTransformWsdlLocations() {
        return true;
    }
    
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/ws/*"};
    }
    
    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        if (servletContext.getAttribute("rootAppCtx") == null) {
            servletContext.addListener(new ContextLoaderListener(rootApplicationContext));
            
            servletContext.setAttribute("rootAppCtx", rootApplicationContext);
        }
    }
}
