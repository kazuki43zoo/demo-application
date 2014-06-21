package com.github.kazuki43zoo.app.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

import com.github.kazuki43zoo.core.validation.AccountId;
import com.github.kazuki43zoo.core.validation.Confirm;
import com.github.kazuki43zoo.core.validation.FirstName;
import com.github.kazuki43zoo.core.validation.LastName;
import com.github.kazuki43zoo.core.validation.Password;

@Confirm(field = "password")
@Data
public class AccountForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @AccountId
    private String accountId;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    @NotNull
    private List<String> authorities;

    @Password
    private String password;

    private String confirmPassword;

    @NotNull
    private Boolean enabled;

    public void addAuthority(String authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(authority);
    }

}
