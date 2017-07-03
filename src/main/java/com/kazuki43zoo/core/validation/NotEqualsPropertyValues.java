package com.kazuki43zoo.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {NotEqualsPropertyValuesValidator.class})
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface NotEqualsPropertyValues {
    String message() default "{com.github.kazuki43zoo.core.validation.NotEqualsPropertyValues.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String property();
    String comparingProperty();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotEqualsPropertyValues[] value();
    }
}