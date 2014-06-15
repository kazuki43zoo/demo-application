package com.github.kazuki43zoo.domain.service.account;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.date.DateFactory;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthority;
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
            throw new ResourceNotFoundException(ResultMessages.error().add("e.xx.fw.5001"));
        }
        return account;
    }

    @Override
    public Account create(Account inputAccount) {
        String rawPassword = inputAccount.getPassword();
        if (!StringUtils.hasLength(rawPassword)) {
            rawPassword = dateFactory.newDateTime().toString("yyyyMMdd");
        }
        inputAccount.setPassword(passwordEncoder.encode(rawPassword));
        inputAccount.setEnabled(true);
        accountRepository.save(inputAccount);
        for (AccountAuthority inputAuthority : inputAccount.getAuthorities()) {
            inputAuthority.setAccountUuid(inputAccount.getAccountUuid());
            accountRepository.createAuthority(inputAuthority);
        }
        return getAccount(inputAccount.getAccountUuid());
    }

    @Override
    public Account changeProfile(Account inputAccount) {

        Account currentAccount = getAccount(inputAccount.getAccountUuid());
        if (StringUtils.hasLength(inputAccount.getPassword())) {
            currentAccount.setPassword(passwordEncoder.encode(inputAccount.getPassword()));
        }
        currentAccount.setAccountId(inputAccount.getAccountId());
        currentAccount.setFirstName(inputAccount.getFirstName());
        currentAccount.setLastName(inputAccount.getLastName());

        accountRepository.save(currentAccount);

        return getAccount(inputAccount.getAccountUuid());
    }

    @Override
    public Account change(Account inputAccount) {

        Account currentAccount = getAccount(inputAccount.getAccountUuid());
        if (StringUtils.hasLength(inputAccount.getPassword())) {
            currentAccount.setPassword(passwordEncoder.encode(inputAccount.getPassword()));
        }
        currentAccount.setAccountId(inputAccount.getAccountId());
        currentAccount.setFirstName(inputAccount.getFirstName());
        currentAccount.setLastName(inputAccount.getLastName());
        currentAccount.setEnabled(inputAccount.isEnabled());

        accountRepository.save(currentAccount);

        for (AccountAuthority currentAuthority : currentAccount.getAuthorities()) {
            if (!inputAccount.getAuthorities().remove(currentAuthority)) {
                accountRepository.deleteAuthority(currentAuthority);
            }
        }
        for (AccountAuthority inputAuthority : inputAccount.getAuthorities()) {
            accountRepository.createAuthority(inputAuthority);
        }

        return getAccount(inputAccount.getAccountUuid());
    }

    @Override
    public void delete(String accountUuid) {
        accountRepository.deleteAuthorities(accountUuid);
        accountRepository.delete(accountUuid);
    }

}
