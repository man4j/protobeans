package org.protobeans.mvc.config;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.ServletContextScope;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
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
    protected Filter[] getServletFilters() {
        return new Filter[] {new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true, true)};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(RequestContextListener.class);//Для того, чтобы запрос был доступен в фильтрах, например в SocialAuthenticationFilter
        
        super.onStartup(servletContext);
        
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
    }
    
    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        if (servletContext.getAttribute("rootAppCtx") == null) {            
            if (rootApplicationContext.getServletContext() == null) {//in case then spring-test already inject MockServletContext
                rootApplicationContext.setServletContext(servletContext);
                
                ConfigurableListableBeanFactory bf = (ConfigurableListableBeanFactory) rootApplicationContext.getAutowireCapableBeanFactory();
                
                //see AbstractRefreshableWebApplicationContext::postProcessBeanFactory
                ServletContextScope appScope = new ServletContextScope(servletContext);
                bf.registerScope(WebApplicationContext.SCOPE_APPLICATION, appScope);
                servletContext.setAttribute(ServletContextScope.class.getName(), appScope);
                
                WebApplicationContextUtils.registerEnvironmentBeans(bf, servletContext, null);
                
                bf.getBeansOfType(ServletContextAware.class).values().forEach(b -> b.setServletContext(servletContext));
            }
            
            servletContext.addListener(new ContextLoaderListener(rootApplicationContext));            
            servletContext.setAttribute("rootAppCtx", rootApplicationContext);
        }
    }
}
