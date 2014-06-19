package com.github.kazuki43zoo.domain.service.account;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.AccountPasswordLock;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;

@Transactional
@Service
public class AccountSharedServiceImpl implements AccountSharedService {

    @Inject
    DateFactory dateFactory;

    @Inject
    AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account getAccount(String accountId) {
        return accountRepository.findOneByAccountId(accountId);
    }

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
            AccountAuthenticationHistory authenticationHistory) {
        Account failedAccount = accountRepository.findOneByAccountId(failedAccountId);
        if (failedAccount == null) {
            return;
        }
        createAuthenticationHistory(failedAccount, authenticationHistory, "login", false);
    }

    public void createLogoutHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory) {
        createAuthenticationHistory(authenticatedAccount, authenticationHistory, "logout", true);
    }

    public void createSessionTimeoutHistory(Account authenticatedAccount,
            AccountAuthenticationHistory authenticationHistory) {
        createAuthenticationHistory(authenticatedAccount, authenticationHistory, "sessionTimeout",
                true);
    }

    @Override
    public void countUpPasswordFailureCount(String failedAccountId) {
        Account failedAccount = accountRepository.findOneByAccountId(failedAccountId);
        if (failedAccount == null) {
            return;
        }
        DateTime currentDateTime = dateFactory.newDateTime();
        AccountPasswordLock currentPasswordLock = failedAccount.getPasswordLock();
        if (currentPasswordLock == null) {
            accountRepository.createPasswordLock(new AccountPasswordLock(failedAccount
                    .getAccountUuid(), 1, currentDateTime.toDate()));
        } else {
            currentPasswordLock.countUpFailureCount();
            currentPasswordLock.setModifiedAt(currentDateTime.toDate());
            accountRepository.updatePasswordLock(currentPasswordLock);
        }
    }

    @Override
    public void clearPasswordLock(Account authenticatedAccount) {
        accountRepository.deletePasswordLock(authenticatedAccount.getAccountUuid());
    }

    private void createAuthenticationHistory(Account account,
            AccountAuthenticationHistory authenticationHistory, String type, boolean result) {
        DateTime currentDateTime = dateFactory.newDateTime();

        authenticationHistory.setAccountUuid(account.getAccountUuid());
        authenticationHistory.setAuthenticationType(type);
        authenticationHistory.setAuthenticationResult(result);
        authenticationHistory.setCreatedAt(currentDateTime.toDate());

        accountRepository.createAuthenticationHistory(authenticationHistory);
    }

}
