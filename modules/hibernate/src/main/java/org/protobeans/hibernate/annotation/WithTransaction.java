package org.protobeans.hibernate.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor = Exception.class)
public @interface WithTransaction {
    @AliasFor(annotation = Transactional.class)
    boolean readOnly() default false;

    @AliasFor(annotation = Transactional.class)
    String transactionManager() default "";
}
