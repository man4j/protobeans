package org.protobeans.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

public class InjectFromProcessor implements BeanPostProcessor {
    @Autowired
    private ApplicationContext ctx;
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        InjectFrom injectFrom = ctx.findAnnotationOnBean(beanName, InjectFrom.class);
        
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
                                    String systemProperty = injectedString.split(":")[1];
                                    
                                    f.set(bean, System.getProperty(systemProperty));
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
