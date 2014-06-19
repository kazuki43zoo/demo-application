package com.github.kazuki43zoo.domain.service.account;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;

public interface AccountSharedService {

    Account getAccount(String accountId);

    void createLoginSuccessHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory);

    void createLoginFailureHistory(String failedAccountId,
            AccountAuthenticationHistory authenticationHistory);

    void createLogoutHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory);

    void createSessionTimeoutHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory);

    void countUpPasswordFailureCount(String failedAccountId);

    void clearPasswordLock(Account authenticatedAccount);

    boolean isLogin(Account account, String sessionId);

}
