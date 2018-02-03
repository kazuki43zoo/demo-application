package com.kazuki43zoo.domain.service.account;

import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.repository.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@lombok.RequiredArgsConstructor
public class AccountSharedServiceImpl implements AccountSharedService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccount(final String accountId) {
        return this.accountRepository.findOneByAccountId(accountId);
    }

}
