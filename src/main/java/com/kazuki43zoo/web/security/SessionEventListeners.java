package com.kazuki43zoo.web.security;

import com.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.kazuki43zoo.domain.model.account.AuthenticationType;
import com.kazuki43zoo.domain.service.security.AuthenticationSharedService;
import com.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;
import com.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.dozer.Mapper;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
@lombok.RequiredArgsConstructor
public class SessionEventListeners {

    private static final String HANDLE_LOGOUT_KEY = SecurityContextLogoutHandler.class.getName()
            .concat(".logout");

    private final AuthenticationSharedService authenticationSharedService;

    private final Mapper beanMapper;

    @EventListener
    @Transactional
    public void onHttpSessionDestroyed(final HttpSessionDestroyedEvent event) {
        final Boolean isHandleLogout = (Boolean) event.getSession().getAttribute(HANDLE_LOGOUT_KEY);
        if (isHandleLogout != null && isHandleLogout) {
            return;
        }
        final List<SecurityContext> securityContexts = event.getSecurityContexts();
        for (final SecurityContext securityContext : securityContexts) {
            final CustomUserDetails userDetails = CustomUserDetails.getInstance(securityContext.getAuthentication());
            final AccountAuthenticationHistory authenticationHistory = this.beanMapper.map(securityContext.getAuthentication().getDetails(), AccountAuthenticationHistory.class);
            this.authenticationSharedService.createAuthenticationSuccessHistory(userDetails.getAccount(), authenticationHistory, AuthenticationType.SESSION_TIMEOUT);
        }
    }

    @Before(value = "execution(* org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler.logout(..))")
    public void handleLogout(final JoinPoint joinPoint) {
        final HttpServletRequest request = HttpServletRequest.class.cast(joinPoint.getArgs()[0]);
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(HANDLE_LOGOUT_KEY, true);
        }
    }

    @EventListener
    public void onSessionFixationProtection(final SessionFixationProtectionEvent event) {
        final CustomAuthenticationDetails authenticationDetails = CustomAuthenticationDetails.class.cast(event.getAuthentication().getDetails());
        authenticationDetails.setSessionId(event.getNewSessionId());
    }

}
