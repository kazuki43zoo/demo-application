package com.github.kazuki43zoo.app.auth;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.github.kazuki43zoo.app.auth.LoginFormControllerAdvice.LoginFormModelAttribute;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ControllerAdvice(annotations = LoginFormModelAttribute.class)
public class LoginFormControllerAdvice {

    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface LoginFormModelAttribute {
    }

    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }

}
