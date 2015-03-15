package com.github.kazuki43zoo.web.security;

import com.github.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.stereotype.Component;

@Component
public final class SessionFixationProtectionEventListener implements
        ApplicationListener<SessionFixationProtectionEvent> {

    @Override
    public void onApplicationEvent(SessionFixationProtectionEvent event) {
        CustomAuthenticationDetails authenticationDetails =
                CustomAuthenticationDetails.class.cast(event.getAuthentication().getDetails());
        authenticationDetails.setSessionId(event.getNewSessionId());
    }

}
