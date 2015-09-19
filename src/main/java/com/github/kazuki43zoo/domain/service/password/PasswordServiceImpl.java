package com.github.kazuki43zoo.domain.service.password;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.model.account.AccountPasswordHistory;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;
import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import javax.inject.Inject;

@Transactional
@Service
public final class PasswordServiceImpl implements PasswordService {

    @Inject
    PasswordEncoder passwordEncoder;

    @Inject
    JodaTimeDateFactory dateFactory;

    @Inject
    AccountRepository accountRepository;

    @Inject
    PasswordSharedService passwordSharedService;

    @Override
    public Account change(final String accountId, final String rawCurrentPassword, final String rawNewPassword) {
        final Account currentAccount = accountRepository.findOneByAccountId(accountId);

        authenticate(currentAccount, rawCurrentPassword);

        passwordSharedService.validatePassword(rawNewPassword, currentAccount);

        final DateTime currentDateTime = dateFactory.newDateTime();

        final String encodedNewPassword = passwordEncoder.encode(rawNewPassword);
        currentAccount.setPassword(encodedNewPassword);
        currentAccount.setPasswordModifiedAt(currentDateTime);
        accountRepository.update(currentAccount);
        passwordSharedService.resetPasswordLock(currentAccount);

        accountRepository.createPasswordHistory(new AccountPasswordHistory(currentAccount.getAccountUuid(), encodedNewPassword, currentDateTime));

        return currentAccount;

    }

    private void authenticate(final Account currentAccount, final String rawPassword) {
        if (currentAccount == null) {
            throw new ResourceNotFoundException(Message.SECURITY_ACCOUNT_NOT_FOUND.resultMessages());
        }
        if (!passwordEncoder.matches(rawPassword, currentAccount.getPassword())) {
            throw new ResourceNotFoundException(Message.SECURITY_ACCOUNT_NOT_FOUND.resultMessages());
        }
    }

}
