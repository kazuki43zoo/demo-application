package com.github.kazuki43zoo.core.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualsPropertyValuesValidator implements ConstraintValidator<EqualsPropertyValues, Object> {

    private EqualsPropertyValues constraint;

    public void initialize(final EqualsPropertyValues constraint) {
        this.constraint = constraint;
    }

    public boolean isValid(final Object bean, final ConstraintValidatorContext context) {
        final BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
        final Object propertyValue = beanWrapper.getPropertyValue(constraint.property());
        final Object comparingPropertyValue = beanWrapper.getPropertyValue(constraint.comparingProperty());
        final boolean matched = ObjectUtils.nullSafeEquals(propertyValue, comparingPropertyValue);
        if (!matched) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(constraint.message()).
                    addPropertyNode(constraint.property()).addConstraintViolation();
        }
        return matched;
    }

}