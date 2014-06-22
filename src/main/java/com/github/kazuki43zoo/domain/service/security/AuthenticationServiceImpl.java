package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;

@Transactional
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    DateFactory dateFactory;

    @Inject
    AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public boolean isLogin(Account account, String sessionId) {
        AccountAuthenticationHistory lastSuccessAuthenticationHistory = accountRepository
                .findOneLastSuccessAuthenticationHistoryByAccountUuid(account.getAccountUuid());
        if (lastSuccessAuthenticationHistory == null) {
            return false;
        }
        if (lastSuccessAuthenticationHistory.getSessionId().equals(sessionId)) {
            return false;
        }
        return "login".equals(lastSuccessAuthenticationHistory.getAuthenticationType());
    }

    @Override
    public void createLoginSuccessHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory) {
        createAuthenticationHistory(authenticatedAccount, authenticationHistory, "login", true);
    }

    @Override
    public void createLoginFailureHistory(String failedAccountId,
            AccountAuthenticationHistory authenticationHistory, String failureReason) {
        Account failedAccount = accountRepository.findOneByAccountId(failedAccountId);
        if (failedAccount == null) {
            return;
        }
        authenticationHistory.setFailureReason(failureReason);
        createAuthenticationHistory(failedAccount, authenticationHistory, "login", false);
    }

    @Override
    public void createLogoutHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory) {
        createAuthenticationHistory(authenticatedAccount, authenticationHistory, "logout", true);
    }

    @Override
    public void createSessionTimeoutHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory) {
        createAuthenticationHistory(authenticatedAccount, authenticationHistory, "sessionTimeout",
                true);
    }

    private void createAuthenticationHistory(Account account,
            AccountAuthenticationHistory authenticationHistory, String type, boolean result) {
        DateTime currentDateTime = dateFactory.newDateTime();

        authenticationHistory.setAccountUuid(account.getAccountUuid());
        authenticationHistory.setAuthenticationType(type);
        authenticationHistory.setAuthenticationResult(result);
        authenticationHistory.setCreatedAt(currentDateTime);

        accountRepository.createAuthenticationHistory(authenticationHistory);
    }

}
