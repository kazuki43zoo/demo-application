package com.github.kazuki43zoo.domain.service.password;

import com.github.kazuki43zoo.domain.model.account.Account;

public interface PasswordService {
    Account change(String accountId, String currentPassword, String newPassword);
}
