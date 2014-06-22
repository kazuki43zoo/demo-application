package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountAuthenticationHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private DateTime createdAt;
    private String authenticationType;
    private boolean authenticationResult;
    private String failureReason;
    private String remoteAddress;
    private String sessionId;
    private String agent;
    private String trackingId;

}
