package org.protobeans.security.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = LoginNotExistsValidator.class)
public @interface LoginNotExists {
    String message() default "{LoginNotExistsValidator.loginExists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
