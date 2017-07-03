package com.kazuki43zoo.domain.model.account;

import org.joda.time.DateTime;

import java.io.Serializable;

@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data
public class AccountAuthenticationHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private DateTime createdAt;
    private AuthenticationType authenticationType;
    private boolean authenticationResult;
    private String failureReason;
    private String remoteAddress;
    private String sessionId;
    private String agent;
    private String trackingId;

}
