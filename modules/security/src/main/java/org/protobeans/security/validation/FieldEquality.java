package org.protobeans.security.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = FieldEqualityValidator.class)
@Documented
public @interface FieldEquality {
    String field1();
    
    String field2();
    
    String message() default "{FieldEqualityValidator.fieldsNotEquals}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
