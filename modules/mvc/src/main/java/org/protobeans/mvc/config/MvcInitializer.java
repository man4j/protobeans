package org.protobeans.mvc.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.ServletContextAwareProcessor;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;

public class MvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    public static ConfigurableWebApplicationContext rootApplicationContext;
    
    public static List<Filter> filters = new ArrayList<>();
    
    public static String sessionCookieName;
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }
    
    @Override
    protected Filter[] getServletFilters() {
        return filters.toArray(new Filter[] {});
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
    
    @Override
    protected WebApplicationContext createServletApplicationContext() {
        return rootApplicationContext;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {        
        servletContext.addListener(RequestContextListener.class);//Для того, чтобы запрос был доступен в фильтрах
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        
        if (sessionCookieName != null && !sessionCookieName.isBlank()) {
            servletContext.getSessionCookieConfig().setName(sessionCookieName);
        }
        
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootApplicationContext);
        
        if (rootApplicationContext.getServletContext() == null) {//in case then spring-test already inject MockServletContext
            rootApplicationContext.setServletContext(servletContext);
        }

        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) rootApplicationContext.getAutowireCapableBeanFactory();
        
        for (BeanPostProcessor pp : bf.getBeanPostProcessors()) {
            if (pp instanceof ServletContextAwareProcessor) {
                ServletContextAwareProcessor p = (ServletContextAwareProcessor) pp;
                
                try {
                    Field f = p.getClass().getDeclaredField("servletContext");
                    
                    f.setAccessible(true);
                    
                    f.set(p, servletContext);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
        super.onStartup(servletContext);
    }
}
