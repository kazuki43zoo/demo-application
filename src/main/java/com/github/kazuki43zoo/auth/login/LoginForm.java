package com.github.kazuki43zoo.auth.login;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginForm implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    private String accountId;
    @NotNull
    private String password;
}
