package com.github.kazuki43zoo.domain.service.account;

import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.repository.account.AccountsSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountSharedService {

    Account getAccount(String accountId);

}
