package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;

public interface AuthenticationService {

    void createAuthenticationSuccessHistory(Account account,
                                            AccountAuthenticationHistory authenticationHistory, AuthenticationType type);

    void createAuthenticationFailureHistory(String failedAccountId,
                                            AccountAuthenticationHistory authenticationHistory, AuthenticationType type,
                                            String failureReason);

}
