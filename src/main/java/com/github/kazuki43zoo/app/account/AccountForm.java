package com.github.kazuki43zoo.app.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import com.github.kazuki43zoo.app.common.validation.Confirm;

@Confirm(field = "password")
@Data
public class AccountForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 128)
    private String accountId;

    @NotNull
    @Size(max = 128)
    private String firstName;

    @Size(max = 128)
    private String lastName;

    @NotNull
    private List<String> authorities;

    @Size(min = 8)
    private String password;

    private String confirmPassword;

    @NotNull
    private Boolean enabled;

    public void addAuthority(String authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(authority);
    }

}
