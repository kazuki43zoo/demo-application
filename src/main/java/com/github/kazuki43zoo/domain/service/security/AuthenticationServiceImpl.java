package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.terasoluna.gfw.common.date.DateFactory;

import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;

@Transactional
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AuthenticationServiceImpl implements AuthenticationService {

    private final @NonNull DateFactory dateFactory;

    private final @NonNull AccountRepository accountRepository;

    @Override
    public boolean isLogin(Account account) {
        AccountAuthenticationHistory lastSuccessAuthenticationHistory = accountRepository
                .findOneLastSuccessAuthenticationHistoryByAccountUuid(account.getAccountUuid());
        if (lastSuccessAuthenticationHistory == null) {
            return false;
        }
        return lastSuccessAuthenticationHistory.getAuthenticationType() == AuthenticationType.LOGIN;
    }

    @Override
    public void createAuthenticationFailureHistory(String failedAccountId,
            AccountAuthenticationHistory authenticationHistory, AuthenticationType type,
            String failureReason) {
        Account failedAccount = accountRepository.findOneByAccountId(failedAccountId);
        if (failedAccount == null) {
            return;
        }
        authenticationHistory.setFailureReason(failureReason);
        createAuthenticationHistory(failedAccount, authenticationHistory, AuthenticationType.LOGIN,
                false);
    }

    @Override
    public void createAuthenticationSuccessHistory(Account account,
            AccountAuthenticationHistory authenticationHistory, AuthenticationType type) {
        createAuthenticationHistory(account, authenticationHistory, type, true);
    }

    private void createAuthenticationHistory(Account account,
            AccountAuthenticationHistory authenticationHistory, AuthenticationType type,
            boolean result) {
        DateTime currentDateTime = dateFactory.newDateTime();

        authenticationHistory.setAccountUuid(account.getAccountUuid());
        authenticationHistory.setAuthenticationType(type);
        authenticationHistory.setAuthenticationResult(result);
        authenticationHistory.setCreatedAt(currentDateTime);

        accountRepository.createAuthenticationHistory(authenticationHistory);
    }
}
