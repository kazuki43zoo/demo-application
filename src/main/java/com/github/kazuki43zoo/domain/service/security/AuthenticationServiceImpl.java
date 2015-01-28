package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import javax.inject.Inject;

@Transactional
@Service
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class AuthenticationServiceImpl implements AuthenticationService {

    @lombok.NonNull
    private final JodaTimeDateFactory dateFactory;

    @lombok.NonNull
    private final AccountRepository accountRepository;

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
