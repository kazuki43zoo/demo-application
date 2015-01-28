package com.github.kazuki43zoo.domain.service.password;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;
import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;

import javax.inject.Inject;

@Transactional
@Service
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class PasswordSharedServiceImpl implements PasswordSharedService {

    @lombok.NonNull
    private final PasswordEncoder passwordEncoder;

    @lombok.NonNull
    private final JodaTimeDateFactory dateFactory;

    @lombok.NonNull
    private final AccountRepository accountRepository;

    @Override
    public void validatePassword(String rawPassword, Account account) {
        if (rawPassword.toLowerCase().contains(account.getAccountId().toLowerCase())) {
            throw new BusinessException(Message.PASSWORD_CONTAINS_ACCOUNT_ID.resultMessages());
        }
        if (account.isPastUsedPassword(rawPassword, passwordEncoder)) {
            throw new BusinessException(Message.PASSWORD_USED_PAST.resultMessages());
        }
    }

    @Override
    public void countUpPasswordFailureCount(String failedAccountId) {

        Account failedAccount = accountRepository.findOneByAccountId(failedAccountId);
        if (failedAccount == null) {
            return;
        }

        DateTime currentDateTime = dateFactory.newDateTime();
        failedAccount.countUpPasswordFailureCount(currentDateTime);
        accountRepository.savePasswordFailureCount(failedAccount);
    }

    @Override
    public void resetPasswordLock(Account account) {
        account.resetPasswordFailureCount();
        accountRepository.savePasswordFailureCount(account);
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public String generateNewPassword() {
        return dateFactory.newDateTime().toString("yyyyMMdd");
    }

}
