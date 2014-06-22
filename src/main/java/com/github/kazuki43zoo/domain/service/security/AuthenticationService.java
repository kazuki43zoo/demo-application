package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;

public interface AuthenticationService {

    void createLoginSuccessHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory);

    void createLoginFailureHistory(String failedAccountId,
            AccountAuthenticationHistory authenticationHistory, String failureReason);

    void createLogoutHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory);

    void createSessionTimeoutHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory);

    boolean isLogin(Account account, String sessionId);

}
