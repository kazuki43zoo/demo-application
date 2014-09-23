package com.github.kazuki43zoo.web.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.stereotype.Component;

import com.github.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;

@Component
public final class SessionFixationProtectionEventListenerImpl implements
        ApplicationListener<SessionFixationProtectionEvent> {

    @Override
    public void onApplicationEvent(SessionFixationProtectionEvent event) {
        Authentication authentication = event.getAuthentication();
        CustomAuthenticationDetails authenticationDetails = (CustomAuthenticationDetails) authentication
                .getDetails();
        authenticationDetails.setSessionId(event.getNewSessionId());
    }

}
