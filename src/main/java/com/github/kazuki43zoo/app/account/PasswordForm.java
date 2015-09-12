package com.github.kazuki43zoo.app.account;

import com.github.kazuki43zoo.core.validation.EqualsPropertyValues;
import com.github.kazuki43zoo.core.validation.Password;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsPropertyValues(property = "password", comparingProperty = "confirmPassword")
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

    @NotNull
    private String confirmPassword;
}
