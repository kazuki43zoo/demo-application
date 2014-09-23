package com.github.kazuki43zoo.app.account;

import com.github.kazuki43zoo.core.validation.AccountId;
import com.github.kazuki43zoo.core.validation.FirstName;
import com.github.kazuki43zoo.core.validation.LastName;

import java.io.Serializable;

@lombok.Data
public final class ProfileForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @AccountId
    private String accountId;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

}
