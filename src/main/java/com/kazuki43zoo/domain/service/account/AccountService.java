package com.kazuki43zoo.domain.service.account;

import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.repository.account.AccountsSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    Page<Account> searchAccounts(AccountsSearchCriteria criteria, Pageable pageable);

    Account getAccount(String accountUuid);

    Account create(Account inputAccount);

    Account changeProfile(Account inputAccount);

    void change(Account inputAccount);

    void delete(String accountUuid);

    void unlock(String accountUuid);

}
