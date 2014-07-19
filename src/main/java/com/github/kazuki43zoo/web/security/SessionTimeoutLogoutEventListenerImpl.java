package com.github.kazuki43zoo.web.security;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.AuthenticationType;
import com.github.kazuki43zoo.domain.service.security.AuthenticationService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;

@Transactional
@Named
@Aspect
public class SessionTimeoutLogoutEventListenerImpl implements
        ApplicationListener<HttpSessionDestroyedEvent> {

    private static final String HANDLE_LOGOUT_KEY = SecurityContextLogoutHandler.class.getName()
            .concat(".logout");

    @Inject
    AuthenticationService authenticationService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        Boolean isHandleLogout = (Boolean) event.getSession().getAttribute(HANDLE_LOGOUT_KEY);
        if (isHandleLogout != null && isHandleLogout.booleanValue()) {
            return;
        }
        List<SecurityContext> securityContexts = event.getSecurityContexts();
        for (SecurityContext securityContext : securityContexts) {
            CustomUserDetails userDetails = (CustomUserDetails) securityContext.getAuthentication()
                    .getPrincipal();
            AccountAuthenticationHistory authenticationHistory = beanMapper.map(securityContext
                    .getAuthentication().getDetails(), AccountAuthenticationHistory.class);
            authenticationService.createAuthenticationSuccessHistory(userDetails.getAccount(),
                    authenticationHistory, AuthenticationType.sessionTimeout);
        }
    }

    @Before(value = "execution(* org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler.logout(..))")
    public void handleLogout(JoinPoint jp) {
        HttpServletRequest request = (HttpServletRequest) jp.getArgs()[0];
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(HANDLE_LOGOUT_KEY, true);
        }
    }

}
