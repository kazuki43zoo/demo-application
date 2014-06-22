package com.github.kazuki43zoo.domain.service.account;


public interface PasswordService {
    void change(String accountId, String oldPassword, String password);
}
