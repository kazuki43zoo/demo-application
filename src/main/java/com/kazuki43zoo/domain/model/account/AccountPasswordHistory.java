package com.kazuki43zoo.domain.model.account;

import org.joda.time.DateTime;

import java.io.Serializable;

@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data
@lombok.ToString(exclude = "password")
public class AccountPasswordHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private String password;
    private DateTime createdAt;

}
