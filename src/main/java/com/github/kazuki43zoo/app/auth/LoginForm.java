package com.github.kazuki43zoo.app.auth;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LoginForm implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    private String accountId;
    @NotNull
    private String password;
}
