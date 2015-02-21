package com.github.kazuki43zoo.app.account;

import com.github.kazuki43zoo.core.validation.Confirm;
import com.github.kazuki43zoo.core.validation.Password;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Confirm(field = "password")
@lombok.Data
@lombok.ToString(exclude = {"oldPassword", "password", "confirmPassword"})
public final class PasswordForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String accountId;

    @NotNull
    private String oldPassword;

    @NotNull
    @Password
    private String password;

    @NotNull
    private String confirmPassword;
}
