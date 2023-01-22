package org.protobeans.security.validation;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldEqualityValidator implements ConstraintValidator<FieldEquality, Object> {
    private String fieldName1;
    
    private String fieldName2;
    
    @Override
    public void initialize(FieldEquality constraintAnnotation) {
        fieldName1 = constraintAnnotation.field1();
        fieldName2 = constraintAnnotation.field2();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Field field1 = ReflectionUtils.findField(obj.getClass(), fieldName1);
            Field field2 = ReflectionUtils.findField(obj.getClass(), fieldName2);
            
            field1.setAccessible(true);
            field2.setAccessible(true);
        
            if (field1.get(obj) == null || field2.get(obj) == null) return true;
        
            if (!field1.get(obj).equals(field2.get(obj))) {
                context.buildConstraintViolationWithTemplate("").addPropertyNode(fieldName1).addConstraintViolation();
                context.buildConstraintViolationWithTemplate("").addPropertyNode(fieldName2).addConstraintViolation();
                
                return false;
            }
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
