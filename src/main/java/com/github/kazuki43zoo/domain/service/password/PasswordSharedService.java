package com.github.kazuki43zoo.domain.service.password;

import com.github.kazuki43zoo.domain.model.Account;

public interface PasswordSharedService {

    void validatePassword(String rawPassword, Account account);

    void countUpPasswordFailureCount(String failedAccountId);

    void resetPasswordLock(Account account);

    String encode(String rawPassword);

    String generateNewPassword();

}
