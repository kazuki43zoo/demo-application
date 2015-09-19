package com.github.kazuki43zoo.domain.service.account;

import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Transactional
@Service
public final class AccountSharedServiceImpl implements AccountSharedService {

    @Inject
    AccountRepository accountRepository;

    @Override
    public Account getAccount(final String accountId) {
        return accountRepository.findOneByAccountId(accountId);
    }

}
