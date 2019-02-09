package org.protobeans.cxf.config;

import java.lang.reflect.Field;
import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionTrackingMode;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.ServletContextAwareProcessor;

public class CxfInitializer implements WebApplicationInitializer {
    public static String SERVLET_PATH;
    
    public static ConfigurableWebApplicationContext rootApplicationContext;
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(RequestContextListener.class);//Для того, чтобы запрос был доступен в фильтрах, например в SocialAuthenticationFilter
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        
        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) rootApplicationContext.getAutowireCapableBeanFactory();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootApplicationContext);

        if (rootApplicationContext.getServletContext() == null) {//in case then spring-test already inject MockServletContext
            rootApplicationContext.setServletContext(servletContext);
        }
            
        //Некоторые классы вроде WebMvcConfigurationSupport зависят от servletContext, но получается, что пост-процессор
        //почему-то срабатывает позже, чем это нужно поэтому распихиваем servletContext вручную
        bf.getBeansOfType(ServletContextAware.class).values().forEach(b -> b.setServletContext(servletContext));
        
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
        
        Dynamic servlet = servletContext.addServlet("cxf", new CXFServlet());
        
        servlet.addMapping(SERVLET_PATH);
        servlet.setInitParameter("static-resources-list", "/wsdl-viewer.xsl");
        servlet.setLoadOnStartup(100);
    }
}
