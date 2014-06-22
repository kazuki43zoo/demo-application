package com.github.kazuki43zoo.domain.service.password;

public interface PasswordService {
    void change(String accountId, String oldPassword, String password);
}
