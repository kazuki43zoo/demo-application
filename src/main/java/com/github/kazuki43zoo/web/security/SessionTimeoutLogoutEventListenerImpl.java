package com.github.kazuki43zoo.web.security;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.security.AuthenticationService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Transactional
@Component
@Aspect
public final class SessionTimeoutLogoutEventListenerImpl implements
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
        if (isHandleLogout != null && isHandleLogout) {
            return;
        }
        List<SecurityContext> securityContexts = event.getSecurityContexts();
        for (SecurityContext securityContext : securityContexts) {
            CustomUserDetails userDetails =
                    CustomUserDetails.getInstance(securityContext.getAuthentication());
            AccountAuthenticationHistory authenticationHistory =
                    beanMapper.map(securityContext.getAuthentication().getDetails(), AccountAuthenticationHistory.class);
            authenticationService.createAuthenticationSuccessHistory(userDetails.getAccount(),
                    authenticationHistory, AuthenticationType.SESSION_TIMEOUT);
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
