package com.github.kazuki43zoo.app.account;

import lombok.Data;

import com.github.kazuki43zoo.core.validation.AccountId;
import com.github.kazuki43zoo.core.validation.Confirm;
import com.github.kazuki43zoo.core.validation.FirstName;
import com.github.kazuki43zoo.core.validation.LastName;
import com.github.kazuki43zoo.core.validation.Password;

@Confirm(field = "password")
@Data
public class ProfileForm {

    @AccountId
    private String accountId;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    @Password
    private String password;

    private String confirmPassword;

}
