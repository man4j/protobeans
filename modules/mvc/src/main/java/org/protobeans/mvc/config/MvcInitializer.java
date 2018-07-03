package org.protobeans.mvc.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    public static ConfigurableWebApplicationContext rootApplicationContext;
    
    public static List<Filter> filters = new ArrayList<>();
    
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
        servletContext.addListener(RequestContextListener.class);//Для того, чтобы запрос был доступен в фильтрах, например в SocialAuthenticationFilter
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));

        if (rootApplicationContext.getServletContext() == null) {//in case then spring-test already inject MockServletContext
            rootApplicationContext.setServletContext(servletContext);
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootApplicationContext);
            
            //Некоторые классы вроде WebMvcConfigurationSupport зависят от servletContext, но получается, что пост-процессор
            //почему-то срабатывает позже, чем это нужно поэтому распихиваем servletContext вручную
            ConfigurableListableBeanFactory bf = (ConfigurableListableBeanFactory) rootApplicationContext.getAutowireCapableBeanFactory();
            bf.getBeansOfType(ServletContextAware.class).values().forEach(b -> b.setServletContext(servletContext));
        }
        
        super.onStartup(servletContext);
    }
}
