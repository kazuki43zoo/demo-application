package com.kazuki43zoo.core.validation;

import org.terasoluna.gfw.common.validator.constraints.Compare;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {})
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Compare(left = "", right = "", operator = Compare.Operator.EQUAL, message = "{com.github.kazuki43zoo.core.validation.EqualsPropertyValues.message}")
public @interface EqualsPropertyValues {
    @OverridesAttribute(constraint = Compare.class, name = "message")
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @OverridesAttribute(constraint = Compare.class, name = "left")
    String property();

    @OverridesAttribute(constraint = Compare.class, name = "right")
    String comparingProperty();

    @OverridesAttribute(constraint = Compare.class, name = "requireBoth")
    boolean requireBoth() default false;

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        EqualsPropertyValues[] value();
    }
}