package com.github.kazuki43zoo.domain.service.password;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountPasswordHistory;
import com.github.kazuki43zoo.domain.model.AccountPasswordLock;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;

@Transactional
@Service
public class PasswordSharedServiceImpl implements PasswordSharedService {

    @Inject
    PasswordEncoder passwordEncoder;

    @Inject
    DateFactory dateFactory;

    @Inject
    AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public void validatePassword(String rawPassword, Account account) {
        if (account.getPasswordHistories() == null) {
            return;
        }
        for (AccountPasswordHistory passwordHistory : account.getPasswordHistories()) {
            if (passwordEncoder.matches(rawPassword, passwordHistory.getPassword())) {
                throw new BusinessException(Message.PASSWORD_USED_PAST.buildResultMessages());
            }
        }
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
                    .getAccountUuid(), 1, currentDateTime));
        } else {
            currentPasswordLock.countUpFailureCount();
            currentPasswordLock.setModifiedAt(currentDateTime);
            accountRepository.updatePasswordLock(currentPasswordLock);
        }
    }

    @Override
    public void clearPasswordLock(Account account) {
        accountRepository.deletePasswordLock(account.getAccountUuid());
        account.setPasswordLock(null);
    }

}
