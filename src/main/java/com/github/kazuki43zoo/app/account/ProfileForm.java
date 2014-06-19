package com.github.kazuki43zoo.app.account;

import lombok.Data;

import com.github.kazuki43zoo.app.account.validation.AccountId;
import com.github.kazuki43zoo.app.account.validation.FirstName;
import com.github.kazuki43zoo.app.account.validation.LastName;
import com.github.kazuki43zoo.app.account.validation.Password;
import com.github.kazuki43zoo.app.common.validation.Confirm;

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
