package com.kazuki43zoo.domain.service.security;

import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.kazuki43zoo.domain.model.account.AuthenticationType;

public interface AuthenticationSharedService {

    void createAuthenticationSuccessHistory(
            Account account,
            AccountAuthenticationHistory authenticationHistory,
            AuthenticationType type);

    void createAuthenticationFailureHistory(
            String failedAccountId,
            AccountAuthenticationHistory authenticationHistory,
            AuthenticationType type,
            String failureReason);

}
