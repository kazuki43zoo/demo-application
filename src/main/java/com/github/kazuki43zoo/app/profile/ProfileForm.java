package com.github.kazuki43zoo.app.profile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import com.github.kazuki43zoo.app.common.validation.Confirm;

@Confirm(field = "password")
@Data
public class ProfileForm {

    @NotNull
    private String accountId;

    @NotNull
    @Size(max = 128)
    private String firstName;

    @Size(max = 128)
    private String lastName;

    @Size(min = 8)
    private String password;

    private String confirmPassword;

}
