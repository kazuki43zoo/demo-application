package com.github.kazuki43zoo.domain.repository.account;

import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AccountAuthority;
import com.github.kazuki43zoo.domain.model.account.AccountPasswordHistory;
import com.github.kazuki43zoo.domain.repository.PageParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountRepository {

    long countByCriteria(@Param("criteria") AccountsSearchCriteria criteria);

    List<Account> findAllByCriteria(@Param("criteria") AccountsSearchCriteria criteria,
                                    @Param("pageParams") PageParams pageParams);

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
