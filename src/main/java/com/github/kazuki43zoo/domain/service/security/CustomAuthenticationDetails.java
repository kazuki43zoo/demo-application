package com.github.kazuki43zoo.domain.service.security;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String remoteAddress;
    private final String requestedSessionId;
    private final String agent;
    private final String trackingId;
    private String sessionId;

}
