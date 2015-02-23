package com.github.kazuki43zoo.app.account;

import com.github.kazuki43zoo.core.validation.AccountId;
import com.github.kazuki43zoo.core.validation.FirstName;
import com.github.kazuki43zoo.core.validation.LastName;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@lombok.Data
public class ProfileForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @AccountId
    private String accountId;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    @NotNull
    private Boolean enabledAutoLogin;

}
