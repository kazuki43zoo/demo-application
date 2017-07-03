package com.kazuki43zoo.domain.model.account;

import java.io.Serializable;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class AccountAuthority implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private String authority;
}
