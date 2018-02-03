package com.kazuki43zoo.domain.service.password;

import com.kazuki43zoo.core.message.Message;
import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.repository.account.AccountRepository;
import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;

@Transactional
@Service
@lombok.RequiredArgsConstructor
public class PasswordSharedServiceImpl implements PasswordSharedService {

    private final PasswordEncoder passwordEncoder;

    private final JodaTimeDateFactory dateFactory;

    private final AccountRepository accountRepository;

    @Override
    public void validatePassword(final String rawPassword, final Account account) {
        if (rawPassword.toLowerCase().contains(account.getAccountId().toLowerCase())) {
            throw new BusinessException(Message.PASSWORD_CONTAINS_ACCOUNT_ID.resultMessages());
        }
        if (account.isPastUsedPassword(rawPassword, this.passwordEncoder)) {
            throw new BusinessException(Message.PASSWORD_USED_PAST.resultMessages());
        }
    }

    @Override
    public void countUpPasswordFailureCount(final String failedAccountId) {

        final Account failedAccount = this.accountRepository.findOneByAccountId(failedAccountId);
        if (failedAccount == null) {
            return;
        }

        final DateTime currentDateTime = this.dateFactory.newDateTime();
        failedAccount.countUpPasswordFailureCount(currentDateTime);
        this.accountRepository.savePasswordFailureCount(failedAccount);
    }

    @Override
    public void resetPasswordLock(final Account account) {
        account.resetPasswordFailureCount();
        this.accountRepository.savePasswordFailureCount(account);
    }

    @Override
    public String encode(final String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    @Override
    public String generateNewPassword() {
        return this.dateFactory.newDateTime().toString("yyyyMMdd");
    }

}
