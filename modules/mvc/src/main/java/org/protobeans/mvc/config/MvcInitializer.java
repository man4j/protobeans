package org.protobeans.mvc.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
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
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(RequestContextListener.class);//Для того, чтобы запрос был доступен в фильтрах, например в SocialAuthenticationFilter
        
        super.onStartup(servletContext);
    }
    
    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        if (servletContext.getAttribute("rootAppCtx") == null) {
            DelegatingWebMvcConfiguration webMvcConfiguration = rootApplicationContext.getBean(DelegatingWebMvcConfiguration.class);
            
            webMvcConfiguration.setServletContext(servletContext);
            
            servletContext.addListener(new ContextLoaderListener(rootApplicationContext));
            
            servletContext.setAttribute("rootAppCtx", rootApplicationContext);
        }
    }
}
