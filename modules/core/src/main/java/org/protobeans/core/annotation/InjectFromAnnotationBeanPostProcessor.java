package org.protobeans.core.annotation;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InjectFromAnnotationBeanPostProcessor implements BeanPostProcessor, PriorityOrdered, ApplicationContextAware {
    private ApplicationContext ctx;
    
    public InjectFromAnnotationBeanPostProcessor() {
        log.info("--------------------------------------------------------------------");
        log.info("                      __        __");                         
        log.info("    ____  _________  / /_____  / /_  ___  ____ _____  _____");
        log.info("   / __ \\/ ___/ __ \\/ __/ __ \\/ __ \\/ _ \\/ __ `/ __ \\/ ___/");
        log.info("  / /_/ / /  / /_/ / /_/ /_/ / /_/ /  __/ /_/ / / / (__  )");
        log.info(" / .___/_/   \\____/\\__/\\____/_.___/\\___/\\__,_/_/ /_/____/");
        log.info("/_/");
        log.info("--------------------------------------------------------------------");
    }
    
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        InjectFrom injectFrom = null;
        
        try {
            injectFrom = ctx.findAnnotationOnBean(beanName, InjectFrom.class);
        } catch (NoSuchBeanDefinitionException e) {
            log.debug(e.getMessage());
        }
        
        if (injectFrom != null) {
            var annotationClass = injectFrom.value();
            
            for (var annotatedBeanName : ctx.getBeanNamesForAnnotation(annotationClass)) {
                var annotation = ctx.findAnnotationOnBean(annotatedBeanName, annotationClass);

                for (var m : annotationClass.getDeclaredMethods()) {
                    try {
                        var f = ReflectionUtils.findField(bean.getClass(), m.getName());
                        
                        if (f != null) {
                            f.setAccessible(true);
                            var injectedValue = m.invoke(annotation);
                            
                            if (injectedValue instanceof String injectedString) {
                                String result;
                                
                                try {
                                    result = (String) resolveExpression(injectedString);
                                } catch (@SuppressWarnings("unused") Exception e) {
                                    result = injectedString;
                                }
                                
                                f.set(bean, result);
                            } else {
                                f.set(bean, injectedValue);
                            }
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        
        return bean;
    }
    
    private Object resolveExpression(String expression) {
        var bf = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();

        var placeholdersResolved = bf.resolveEmbeddedValue(expression);
        
        var expressionResolver = bf.getBeanExpressionResolver();
        
        return expressionResolver.evaluate(placeholdersResolved, new BeanExpressionContext(bf, null));
    }
}
