package com.kazuki43zoo.web.security;

import com.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;
import com.kazuki43zoo.web.CustomHttpHeaders;
import com.google.common.net.HttpHeaders;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public final class CustomAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, CustomAuthenticationDetails> {

    @Override
    public CustomAuthenticationDetails buildDetails(final HttpServletRequest request) {
        final String forwardedFor = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        final String remoteAddress;
        if (forwardedFor != null) {
            remoteAddress = forwardedFor;
        } else {
            remoteAddress = request.getRemoteAddr();
        }
        final HttpSession session = request.getSession(false);
        final String requestedSessionId = (session != null) ? session.getId() : null;
        final String agent = request.getHeader(HttpHeaders.USER_AGENT);
        final String trackingId = String.class.cast(request.getAttribute(CustomHttpHeaders.X_TRACK));
        return new CustomAuthenticationDetails(remoteAddress, requestedSessionId, agent, trackingId);
    }

}
