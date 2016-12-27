package org.protobeans.mvc.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

public class ProtoBeansDefinitionScanner {
    private Class<?>[] basePackageClasses;

    public ProtoBeansDefinitionScanner(Class<?>[] basePackageClasses) {
        this.basePackageClasses = basePackageClasses;
    }

    public void scan(ConfigurableApplicationContext ctx) {
        if (basePackageClasses.length > 0) {
            ClassPathBeanDefinitionScanner beanDefinitionScanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) ctx.getBeanFactory());

            beanDefinitionScanner.scan(Stream.of(basePackageClasses).map(c -> c.getPackage().getName()).collect(Collectors.toList()).toArray(new String[] {}));
        }
    }
}
