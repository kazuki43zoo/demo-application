package com.github.kazuki43zoo.domain.service.security;

import java.io.Serializable;

@lombok.Data
@lombok.RequiredArgsConstructor
public final class CustomAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String remoteAddress;
    private final String requestedSessionId;
    private final String agent;
    private final String trackingId;
    private String sessionId;

}
