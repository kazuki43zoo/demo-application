package com.github.kazuki43zoo.domain.service.account;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import com.github.kazuki43zoo.core.message.Messages;
import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthority;
import com.github.kazuki43zoo.domain.model.AccountPasswordHistory;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;
import com.github.kazuki43zoo.domain.repository.account.AccountsSearchCriteria;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    @Inject
    Mapper beanMapper;

    @Inject
    PasswordEncoder passwordEncoder;

    @Inject
    DateFactory dateFactory;

    @Inject
    AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Account> searchAccounts(AccountsSearchCriteria criteria, Pageable pageable) {
        criteria.determineCriteria();
        long totalCount = accountRepository.countByCriteria(criteria);
        List<Account> accounts;
        if (totalCount == 0) {
            accounts = Collections.emptyList();
        } else {
            accounts = accountRepository.findAllByCriteria(criteria, pageable);
        }
        return new PageImpl<>(accounts, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    @Override
    public Account getAccount(String accountUuid) {
        Account account = accountRepository.findOne(accountUuid);
        if (account == null) {
            throw new ResourceNotFoundException(Messages.FW_NOT_FOUND.buildResultMessages());
        }
        return account;
    }

    @Override
    public Account create(Account inputAccount) {
        DateTime currentDateTime = dateFactory.newDateTime();

        String rawPassword = inputAccount.getPassword();
        if (!StringUtils.hasLength(rawPassword)) {
            rawPassword = currentDateTime.toString("yyyyMMdd");
        }
        validatePassword(rawPassword, inputAccount);

        String encodedPassword = passwordEncoder.encode(rawPassword);
        inputAccount.setPassword(encodedPassword);
        inputAccount.setPasswordModifiedAt(currentDateTime.toDate());
        inputAccount.setEnabled(true);

        accountRepository.create(inputAccount);

        String accountUuid = inputAccount.getAccountUuid();
        for (AccountAuthority inputAuthority : inputAccount.getAuthorities()) {
            inputAuthority.setAccountUuid(accountUuid);
            accountRepository.createAuthority(inputAuthority);
        }
        accountRepository.createPasswordHistory(new AccountPasswordHistory(accountUuid,
                encodedPassword, currentDateTime.toDate()));

        return getAccount(accountUuid);
    }

    @Override
    public Account changeProfile(Account inputAccount) {
        DateTime currentDateTime = dateFactory.newDateTime();
        String accountUuid = inputAccount.getAccountUuid();

        Account currentAccount = getAccount(accountUuid);

        currentAccount.setAccountId(inputAccount.getAccountId());
        currentAccount.setFirstName(inputAccount.getFirstName());
        currentAccount.setLastName(inputAccount.getLastName());

        AccountPasswordHistory passwordHistory = null;
        String rawPassword = inputAccount.getPassword();
        if (StringUtils.hasLength(rawPassword)) {
            validatePassword(rawPassword, currentAccount);
            String encodedPassword = passwordEncoder.encode(rawPassword);
            currentAccount.setPassword(encodedPassword);
            currentAccount.setPasswordModifiedAt(currentDateTime.toDate());
            passwordHistory = new AccountPasswordHistory(accountUuid, encodedPassword,
                    currentDateTime.toDate());
        }

        accountRepository.update(currentAccount);
        if (passwordHistory != null) {
            accountRepository.createPasswordHistory(passwordHistory);
        }

        return getAccount(accountUuid);
    }

    @Override
    public Account change(Account inputAccount) {
        DateTime currentDateTime = dateFactory.newDateTime();
        String accountUuid = inputAccount.getAccountUuid();

        Account currentAccount = getAccount(accountUuid);

        currentAccount.setAccountId(inputAccount.getAccountId());
        currentAccount.setFirstName(inputAccount.getFirstName());
        currentAccount.setLastName(inputAccount.getLastName());
        currentAccount.setEnabled(inputAccount.isEnabled());

        AccountPasswordHistory passwordHistory = null;
        String rawPassword = inputAccount.getPassword();
        if (StringUtils.hasLength(rawPassword)) {
            validatePassword(rawPassword, currentAccount);
            String encodedPassword = passwordEncoder.encode(rawPassword);
            currentAccount.setPassword(encodedPassword);
            currentAccount.setPasswordModifiedAt(currentDateTime.toDate());
            passwordHistory = new AccountPasswordHistory(accountUuid, encodedPassword,
                    currentDateTime.toDate());
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

        return getAccount(accountUuid);
    }

    @Override
    public void delete(String accountUuid) {
        accountRepository.deletePasswordHistories(accountUuid);
        accountRepository.deleteAuthorities(accountUuid);
        accountRepository.delete(accountUuid);
    }

    private void validatePassword(String rawPassword, Account account) {
        if (account.getPasswordHistories() == null) {
            return;
        }
        for (AccountPasswordHistory passwordHistory : account.getPasswordHistories()) {
            if (passwordEncoder.matches(rawPassword, passwordHistory.getPassword())) {
                throw new BusinessException(Messages.ACCOUNT_PASSWORD_USED_PAST.buildResultMessages());
            }
        }

    }

}
