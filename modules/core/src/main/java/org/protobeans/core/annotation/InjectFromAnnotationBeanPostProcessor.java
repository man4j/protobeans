package org.protobeans.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

public class InjectFromAnnotationBeanPostProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(InjectFromAnnotationBeanPostProcessor.class);
    
    @Autowired
    private ApplicationContext ctx;
    
    public InjectFromAnnotationBeanPostProcessor() {
        System.out.println();
        System.out.println("                      __        __");                         
        System.out.println("    ____  _________  / /_____  / /_  ___  ____ _____  _____");
        System.out.println("   / __ \\/ ___/ __ \\/ __/ __ \\/ __ \\/ _ \\/ __ `/ __ \\/ ___/");
        System.out.println("  / /_/ / /  / /_/ / /_/ /_/ / /_/ /  __/ /_/ / / / (__  )");
        System.out.println(" / .___/_/   \\____/\\__/\\____/_.___/\\___/\\__,_/_/ /_/____/");
        System.out.println("/_/");
        System.out.println();
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        InjectFrom injectFrom = null;
        
        try {
            injectFrom = ctx.findAnnotationOnBean(beanName, InjectFrom.class);
        } catch (NoSuchBeanDefinitionException e) {
            logger.debug(e.getMessage());
        }
        
        if (injectFrom != null) {
            Class<? extends Annotation> annotationClass = injectFrom.value();
            
            for (String annotatedBeanName : ctx.getBeanNamesForAnnotation(annotationClass)) {
                Annotation annotation = ctx.findAnnotationOnBean(annotatedBeanName, annotationClass);

                for (Method m : annotationClass.getDeclaredMethods()) {
                    try {
                        Field f = ReflectionUtils.findField(bean.getClass(), m.getName());
                        
                        if (f != null) {
                            f.setAccessible(true);
                            Object injectedValue = m.invoke(annotation);
                            
                            if (injectedValue instanceof String) {
                                String injectedString = (String) injectedValue;
                                
                                if (injectedString.startsWith("s:") || injectedString.startsWith("e:") || injectedString.startsWith("p:")) {
                                    String propName = injectedString.split(":")[1];
                                    
                                    Optional<String> value = Stream.of(ctx.getEnvironment().getProperty(propName),
                                                                       ctx.getEnvironment().getProperty(propName.toLowerCase()), 
                                                                       ctx.getEnvironment().getProperty(propName.toUpperCase())).filter(v -> v != null).findFirst();
                                    
                                    if (!value.isPresent()) {
                                        throw new IllegalStateException("Can not find system property or env variable: " + propName);
                                    }
                                    
                                    f.set(bean, value.get());
                                } else {
                                    String result;
                                    
                                    try {
                                        result = (String) resolveExpression(ctx, injectedString);
                                    } catch (@SuppressWarnings("unused") Exception e) {
                                        result = injectedString;
                                    }
                                    
                                    f.set(bean, result);
                                }
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

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
    private Object resolveExpression(ApplicationContext ctx, String expression) {
        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();

        String placeholdersResolved = bf.resolveEmbeddedValue(expression);
        BeanExpressionResolver expressionResolver = bf.getBeanExpressionResolver();
        
        return expressionResolver.evaluate(placeholdersResolved, new BeanExpressionContext(bf, null));
    }
}
