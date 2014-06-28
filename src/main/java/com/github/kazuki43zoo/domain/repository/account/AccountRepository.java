package com.github.kazuki43zoo.domain.repository.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.AccountAuthority;
import com.github.kazuki43zoo.domain.model.AccountPasswordHistory;

public interface AccountRepository {

    long countByCriteria(@Param("criteria") AccountsSearchCriteria criteria);

    List<Account> findAllByCriteria(@Param("criteria") AccountsSearchCriteria criteria,
            @Param("pageable") Pageable pageable);

    Account findOneByAccountId(String accountId);

    Account findOne(String accountUuid);

    boolean create(Account account);

    boolean update(Account account);

    boolean delete(String accountUuid);

    boolean createAuthority(AccountAuthority authority);

    boolean deleteAuthority(@Param("accountUuid") String accountUuid,
            @Param("authority") String authority);

    int deleteAuthorities(String accountUuid);

    boolean createPasswordHistory(AccountPasswordHistory passwordHistory);

    int deletePasswordHistories(String accountUuid);

    boolean savePasswordFailureCount(Account account);

    boolean deletePasswordLock(String accountUuid);

    boolean createAuthenticationHistory(AccountAuthenticationHistory authenticationHistory);

    int deleteAuthenticationHistories(String accountUuid);

    AccountAuthenticationHistory findOneLastSuccessAuthenticationHistoryByAccountUuid(
            String accountUuid);

}
