package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;

@Data
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private String accountId;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private List<AccountAuthority> authorities;
    @Getter
    private boolean keyGen;

    public void setId(String id) {
        this.keyGen = accountUuid == null;
        this.accountUuid = id;
    }

    public void addAuthority(AccountAuthority authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(authority);
    }

}
