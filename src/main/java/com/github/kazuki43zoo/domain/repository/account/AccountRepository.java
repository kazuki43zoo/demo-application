package com.github.kazuki43zoo.domain.repository.account;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.AccountAuthority;
import com.github.kazuki43zoo.domain.model.AccountPasswordHistory;
import com.github.kazuki43zoo.domain.model.AccountPasswordLock;

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

    boolean createPasswordHistory(AccountPasswordHistory passwordHistory);

    boolean deletePasswordHistories(String accountUuid);

    boolean createPasswordLock(AccountPasswordLock passwordLock);

    boolean updatePasswordLock(AccountPasswordLock passwordLock);

    boolean deletePasswordLock(String accountUuid);

    boolean createAuthenticationHistory(AccountAuthenticationHistory authenticationHistory);

    AccountAuthenticationHistory findOneLastSuccessAuthenticationHistoryByAccountUuid(
            String accountUuid);

}
