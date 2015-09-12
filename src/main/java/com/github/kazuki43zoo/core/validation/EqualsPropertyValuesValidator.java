package com.github.kazuki43zoo.core.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualsPropertyValuesValidator implements ConstraintValidator<EqualsPropertyValues, Object> {

    private EqualsPropertyValues constraint;

    public void initialize(EqualsPropertyValues constraint) {
        this.constraint = constraint;
    }

    public boolean isValid(Object bean, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
        Object propertyValue = beanWrapper.getPropertyValue(constraint.property());
        Object comparingPropertyValue = beanWrapper.getPropertyValue(constraint.comparingProperty());
        boolean matched = ObjectUtils.nullSafeEquals(propertyValue, comparingPropertyValue);
        if (matched) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(constraint.message()).
                    addPropertyNode(constraint.property()).addConstraintViolation();
            return false;
        }
    }

}