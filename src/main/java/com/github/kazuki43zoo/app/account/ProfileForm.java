package com.github.kazuki43zoo.app.account;

import lombok.Data;

import com.github.kazuki43zoo.core.validation.AccountId;
import com.github.kazuki43zoo.core.validation.FirstName;
import com.github.kazuki43zoo.core.validation.LastName;

@Data
public class ProfileForm {

    @AccountId
    private String accountId;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

}
