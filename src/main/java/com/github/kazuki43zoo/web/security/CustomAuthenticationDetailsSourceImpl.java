package com.github.kazuki43zoo.web.security;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import com.github.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;
import com.google.common.net.HttpHeaders;

@Named("customAuthenticationDetailsSource")
public class CustomAuthenticationDetailsSourceImpl implements
        AuthenticationDetailsSource<HttpServletRequest, CustomAuthenticationDetails> {

    @Override
    public CustomAuthenticationDetails buildDetails(HttpServletRequest request) {
        String forwardedFor = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        String remoteAddress = null;
        if (forwardedFor != null) {
            remoteAddress = forwardedFor;
        } else {
            remoteAddress = request.getRemoteAddr();
        }
        HttpSession session = request.getSession(false);
        String sessionId = (session != null) ? session.getId() : null;
        String agent = request.getHeader(HttpHeaders.USER_AGENT);
        String trackingId = (String) request.getAttribute("X-Track");
        return new CustomAuthenticationDetails(remoteAddress, sessionId, agent, trackingId);
    }

}
