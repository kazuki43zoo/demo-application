package com.kazuki43zoo.app.account;

import com.kazuki43zoo.core.validation.EqualsPropertyValues;
import com.kazuki43zoo.core.validation.NotEqualsPropertyValues;
import com.kazuki43zoo.core.validation.Password;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NotEqualsPropertyValues(property = "password", comparingProperty = "username", message = "{NotEqualsWithAccountId.message}")
@NotEqualsPropertyValues(property = "password", comparingProperty = "currentPassword", message = "{NotEqualsWithCurrentPassword.message}")
@EqualsPropertyValues(property = "confirmPassword", comparingProperty = "password", message = "{EqualsWithPassword.message}")
@lombok.Data
@lombok.ToString(exclude = {"currentPassword", "password", "confirmPassword"})
public class PasswordForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String username;

    @NotNull
    private String currentPassword;

    @NotNull
    @Password
    private String password;

    private String confirmPassword;
}
