package com.github.kazuki43zoo.web.security;

import java.util.regex.Pattern;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Named("defaultCsrfRequiresMethodMatcher")
public class DefaultCsrfRequiresMethodMatcherImpl implements RequestMatcher {

    private Pattern allowedMethods;

    @Value("${DefaultCsrfRequiresMethodMatcherImpl.allowedMethods:^(GET|HEAD|TRACE|OPTIONS)$}")
    public void setAllowedMethods(Pattern allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public boolean matches(HttpServletRequest request) {
        return !allowedMethods.matcher(request.getMethod()).matches();
    }

}
