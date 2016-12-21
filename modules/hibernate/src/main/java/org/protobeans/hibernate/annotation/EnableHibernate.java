package org.protobeans.hibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.dialect.Dialect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.protobeans.hibernate.config.HibernateConfig;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(HibernateConfig.class)
@Configuration
public @interface EnableHibernate {
    String showSql();
    
    Class<?>[] basePackageClasses();
    
    Class<? extends Dialect> dialect();
}
