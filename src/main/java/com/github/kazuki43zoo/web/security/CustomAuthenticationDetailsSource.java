package com.github.kazuki43zoo.web.security;

import com.github.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;
import com.github.kazuki43zoo.web.CustomHttpHeaders;
import com.google.common.net.HttpHeaders;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public final class CustomAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, CustomAuthenticationDetails> {

    @Override
    public CustomAuthenticationDetails buildDetails(HttpServletRequest request) {
        String forwardedFor = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        String remoteAddress;
        if (forwardedFor != null) {
            remoteAddress = forwardedFor;
        } else {
            remoteAddress = request.getRemoteAddr();
        }
        HttpSession session = request.getSession(false);
        String requestedSessionId = (session != null) ? session.getId() : null;
        String agent = request.getHeader(HttpHeaders.USER_AGENT);
        String trackingId = String.class.cast(request.getAttribute(CustomHttpHeaders.X_TRACK));
        return new CustomAuthenticationDetails(remoteAddress, requestedSessionId, agent, trackingId);
    }

}
