package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private String accountId;
    private String password;
    private DateTime passwordModifiedAt;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private AccountPasswordLock passwordLock;
    private List<AccountAuthority> authorities;
    private List<AccountPasswordHistory> passwordHistories;
    private List<AccountAuthenticationHistory> authenticationHistories;

    public void addAuthority(AccountAuthority authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(authority);
    }

}
