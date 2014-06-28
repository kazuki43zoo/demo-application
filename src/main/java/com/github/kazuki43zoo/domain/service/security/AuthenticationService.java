package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.AuthenticationType;

public interface AuthenticationService {

    void createAuthenticationSuccessHistory(Account account,
            AccountAuthenticationHistory authenticationHistory, AuthenticationType type);

    void createAuthenticationFailureHistory(String failedAccountId,
            AccountAuthenticationHistory authenticationHistory, AuthenticationType type,
            String failureReason);

    boolean isLogin(Account account);

}
