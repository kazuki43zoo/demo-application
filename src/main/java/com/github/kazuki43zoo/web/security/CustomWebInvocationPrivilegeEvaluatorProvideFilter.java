package com.github.kazuki43zoo.web.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public final class CustomWebInvocationPrivilegeEvaluatorProvideFilter implements Filter,
        WebInvocationPrivilegeEvaluator, BeanPostProcessor {

    private List<WebInvocationPrivilegeEvaluator> webInvocationPrivilegeEvaluators = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        request.setAttribute(WebAttributes.WEB_INVOCATION_PRIVILEGE_EVALUATOR_ATTRIBUTE, this);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        webInvocationPrivilegeEvaluators.clear();
    }

    @Override
    public boolean isAllowed(
            String uri,
            Authentication authentication) {
        for (WebInvocationPrivilegeEvaluator privilegeEvaluator : webInvocationPrivilegeEvaluators) {
            if (!privilegeEvaluator.isAllowed(uri, authentication)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isAllowed(
            String contextPath,
            String uri,
            String method,
            Authentication authentication) {
        for (WebInvocationPrivilegeEvaluator privilegeEvaluator : webInvocationPrivilegeEvaluators) {
            if (!privilegeEvaluator.isAllowed(contextPath, uri, method, authentication)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object postProcessBeforeInitialization(
            Object bean,
            String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName) throws BeansException {
        if (bean instanceof WebInvocationPrivilegeEvaluator
                && !bean.getClass().isAssignableFrom(getClass())) {
            webInvocationPrivilegeEvaluators.add((WebInvocationPrivilegeEvaluator) bean);
        }
        return bean;
    }

}
