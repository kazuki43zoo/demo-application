package com.github.kazuki43zoo.app.account;

import com.github.kazuki43zoo.core.validation.EqualsPropertyValues;
import com.github.kazuki43zoo.core.validation.NotEqualsPropertyValues;
import com.github.kazuki43zoo.core.validation.Password;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NotEqualsPropertyValues.List({
        @NotEqualsPropertyValues(property = "password", comparingProperty = "username")
        , @NotEqualsPropertyValues(property = "password", comparingProperty = "currentPassword")
})
@EqualsPropertyValues(property = "confirmPassword", comparingProperty = "password")
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
