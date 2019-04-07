package com.kazuki43zoo.domain.repository.account;

import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.kazuki43zoo.domain.model.account.AccountAuthority;
import com.kazuki43zoo.domain.model.account.AccountPasswordHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountRepository {

    long countByCriteria(@Param("criteria") AccountsSearchCriteria criteria);

    List<Account> findAllByCriteria(
            @Param("criteria") AccountsSearchCriteria criteria,
            RowBounds rowBounds);

    Account findOneByAccountId(String accountId);

    Account findOne(String accountUuid);

    void lockByAccountIdWithinTransaction(String accountUuid);

    void create(Account account);

    void update(Account account);

    void delete(String accountUuid);

    void createAuthority(AccountAuthority authority);

    void deleteAuthority(
            @Param("accountUuid") String accountUuid,
            @Param("authority") String authority);

    void deleteAuthorities(String accountUuid);

    void createPasswordHistory(AccountPasswordHistory passwordHistory);

    void deletePasswordHistories(String accountUuid);

    void savePasswordFailureCount(Account account);

    void deletePasswordLock(String accountUuid);

    void createAuthenticationHistory(AccountAuthenticationHistory authenticationHistory);

    void deleteAuthenticationHistories(String accountUuid);

    AccountAuthenticationHistory findOneLastSuccessAuthenticationHistoryByAccountUuid(String accountUuid);

}
