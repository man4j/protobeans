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
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

public class InjectFromAnnotationBeanPostProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(InjectFromAnnotationBeanPostProcessor.class);
    
    @Autowired
    private ApplicationContext ctx;
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        InjectFrom injectFrom = null;
        
        try {
            injectFrom = ctx.findAnnotationOnBean(beanName, InjectFrom.class);
        } catch (NoSuchBeanDefinitionException e) {
            logger.warn(e.getMessage());
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
                                
                                if (injectedString.startsWith("s:")) {
                                    String propName = injectedString.split(":")[1];
                                    
                                    Optional<String> value = Stream.of(System.getProperty(propName.toLowerCase()), 
                                                                       System.getProperty(propName.toUpperCase()), 
                                                                       System.getenv(propName.toLowerCase()), 
                                                                       System.getenv(propName.toUpperCase())).filter(v -> v != null).findFirst();
                                    
                                    if (!value.isPresent()) {
                                        throw new IllegalStateException("Can not find system property or env variable: " + propName);
                                    }
                                    
                                    f.set(bean, value.get());
                                } else {
                                    f.set(bean, injectedString);
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
}
