package com.kazuki43zoo.app.account;

import com.kazuki43zoo.core.validation.AccountId;
import com.kazuki43zoo.core.validation.EqualsPropertyValues;
import com.kazuki43zoo.core.validation.FirstName;
import com.kazuki43zoo.core.validation.LastName;
import com.kazuki43zoo.core.validation.NotEqualsPropertyValues;
import com.kazuki43zoo.core.validation.Password;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NotEqualsPropertyValues(property = "password", comparingProperty = "accountId", message = "{NotEqualsWithAccountId.message}")
@EqualsPropertyValues(property = "confirmPassword", comparingProperty = "password", message = "{EqualsWithPassword.message}")
@lombok.Data
@lombok.ToString(exclude = {"password", "confirmPassword"})
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

    @NotNull
    private Boolean enabledAutoLogin;

    public void addAuthority(String authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(authority);
    }

}
