package com.github.kazuki43zoo.web.security;

import javax.inject.Named;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;

import com.github.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;

@Named
public class SessionFixationProtectionEventListenerImpl implements
        ApplicationListener<SessionFixationProtectionEvent> {

    @Override
    public void onApplicationEvent(SessionFixationProtectionEvent event) {
        Authentication authentication = event.getAuthentication();
        CustomAuthenticationDetails authenticationDetails = (CustomAuthenticationDetails) authentication
                .getDetails();
        authenticationDetails.setSessionId(event.getNewSessionId());
    }

}
