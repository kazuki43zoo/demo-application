package com.github.kazuki43zoo.domain.service.account;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.model.account.AccountAuthority;
import com.github.kazuki43zoo.domain.model.account.AccountPasswordHistory;
import com.github.kazuki43zoo.domain.repository.PageParams;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;
import com.github.kazuki43zoo.domain.repository.account.AccountsSearchCriteria;
import com.github.kazuki43zoo.domain.service.password.PasswordSharedService;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class AccountServiceImpl implements AccountService {

    @lombok.NonNull
    private final JodaTimeDateFactory dateFactory;

    @lombok.NonNull
    private final AccountRepository accountRepository;

    @lombok.NonNull
    private final PasswordSharedService passwordSharedService;

    @Override
    public Page<Account> searchAccounts(AccountsSearchCriteria criteria, Pageable pageable) {
        criteria.determineCriteria();
        long totalCount = accountRepository.countByCriteria(criteria);
        List<Account> accounts;
        if (totalCount == 0) {
            accounts = Collections.emptyList();
        } else {
            accounts = accountRepository.findAllByCriteria(criteria, new PageParams(pageable));
        }
        return new PageImpl<>(accounts, pageable, totalCount);
    }

    @Override
    public Account getAccount(String accountUuid) {
        Account account = accountRepository.findOne(accountUuid);
        if (account == null) {
            throw new ResourceNotFoundException(Message.FW_NOT_FOUND.resultMessages());
        }
        return account;
    }

    @Override
    public Account create(Account inputAccount) {
        DateTime currentDateTime = dateFactory.newDateTime();

        String rawPassword = inputAccount.getPassword();
        if (!StringUtils.hasLength(rawPassword)) {
            rawPassword = passwordSharedService.generateNewPassword();
        }
        passwordSharedService.validatePassword(rawPassword, inputAccount);

        String encodedPassword = passwordSharedService.encode(rawPassword);
        inputAccount.setPassword(encodedPassword);
        inputAccount.setPasswordModifiedAt(currentDateTime);
        accountRepository.create(inputAccount);

        String accountUuid = inputAccount.getAccountUuid();
        for (AccountAuthority inputAuthority : inputAccount.getAuthorities()) {
            inputAuthority.setAccountUuid(accountUuid);
            accountRepository.createAuthority(inputAuthority);
        }
        accountRepository.createPasswordHistory(new AccountPasswordHistory(accountUuid,
                encodedPassword, currentDateTime));

        return getAccount(accountUuid);
    }

    @Override
    public Account changeProfile(Account inputAccount) {
        String accountUuid = inputAccount.getAccountUuid();

        Account currentAccount = getAccount(accountUuid);

        currentAccount.setAccountId(inputAccount.getAccountId());
        currentAccount.setFirstName(inputAccount.getFirstName());
        currentAccount.setLastName(inputAccount.getLastName());
        accountRepository.update(currentAccount);

        return getAccount(accountUuid);
    }

    @Override
    public void change(Account inputAccount) {
        String accountUuid = inputAccount.getAccountUuid();

        Account currentAccount = getAccount(accountUuid);

        DateTime currentDateTime = dateFactory.newDateTime();

        currentAccount.setAccountId(inputAccount.getAccountId());
        currentAccount.setFirstName(inputAccount.getFirstName());
        currentAccount.setLastName(inputAccount.getLastName());
        currentAccount.setEnabled(inputAccount.isEnabled());

        AccountPasswordHistory passwordHistory = null;
        String rawPassword = inputAccount.getPassword();
        if (StringUtils.hasLength(rawPassword)) {
            passwordSharedService.validatePassword(rawPassword, currentAccount);
            String encodedPassword = passwordSharedService.encode(rawPassword);
            currentAccount.setPassword(encodedPassword);
            currentAccount.setPasswordModifiedAt(currentDateTime);
            passwordHistory = new AccountPasswordHistory(accountUuid, encodedPassword,
                    currentDateTime);
        }
        accountRepository.update(currentAccount);

        for (AccountAuthority currentAuthority : currentAccount.getAuthorities()) {
            if (!inputAccount.getAuthorities().remove(currentAuthority)) {
                accountRepository.deleteAuthority(currentAuthority.getAccountUuid(),
                        currentAuthority.getAuthority());
            }
        }
        for (AccountAuthority inputAuthority : inputAccount.getAuthorities()) {
            accountRepository.createAuthority(inputAuthority);
        }

        if (passwordHistory != null) {
            accountRepository.createPasswordHistory(passwordHistory);
        }

    }

    @Override
    public void delete(String accountUuid) {
        accountRepository.deleteAuthenticationHistories(accountUuid);
        accountRepository.deletePasswordHistories(accountUuid);
        accountRepository.deletePasswordLock(accountUuid);
        accountRepository.deleteAuthorities(accountUuid);
        accountRepository.delete(accountUuid);
    }

    @Override
    public void unlock(String accountUuid) {
        Account account = getAccount(accountUuid);
        passwordSharedService.resetPasswordLock(account);
    }

}
