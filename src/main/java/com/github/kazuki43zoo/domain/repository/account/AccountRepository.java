package com.github.kazuki43zoo.domain.repository.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthority;

public interface AccountRepository {

    long countByCriteria(@Param("criteria") AccountsSearchCriteria criteria);

    List<Account> findAllByCriteria(@Param("criteria") AccountsSearchCriteria criteria,
            @Param("pageable") Pageable pageable);

    Account findOneByAccountId(String accountId);

    Account findOne(String accountUuid);

    boolean save(Account account);

    boolean delete(String accountUuid);

    boolean createAuthority(AccountAuthority authority);

    boolean deleteAuthority(AccountAuthority authority);

    boolean deleteAuthorities(String accountUuid);

}
