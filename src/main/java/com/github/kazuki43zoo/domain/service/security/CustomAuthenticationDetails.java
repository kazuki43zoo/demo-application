package com.github.kazuki43zoo.domain.service.security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String remoteAddress;
    private final String sessionId;
    private final String agent;
    private final String trackingId;

}
