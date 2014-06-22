package com.github.kazuki43zoo.domain.service.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.repository.account.AccountsSearchCriteria;

public interface AccountService {

    Page<Account> searchAccounts(AccountsSearchCriteria criteria, Pageable pageable);

    Account getAccount(String accountUuid);

    Account create(Account inputAccount);

    Account changeProfile(Account inputAccount);

    void change(Account inputAccount);

    void delete(String accountUuid);

    void unlock(String accountUuid);

}
