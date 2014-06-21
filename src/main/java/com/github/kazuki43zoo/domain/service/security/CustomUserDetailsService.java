package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;

import com.github.kazuki43zoo.core.config.SecurityConfigs;
import com.github.kazuki43zoo.core.message.Messages;
import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.service.account.AccountSharedService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Inject
    AccountSharedService accountSharedService;

    @Inject
    MessageSource messageSource;

    @Inject
    DateFactory dateFactory;

    @Inject
    SecurityConfigs securityConfigs;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountSharedService.getAccount(username);
        if (account == null) {
            throw new UsernameNotFoundException(
                    Messages.SECURITY_ACCOUNT_NOT_FOUND.buildMessage(messageSource));
        }

        DateTime currentDateTime = dateFactory.newDateTime();
        DateTime passwordModifiedAt = new DateTime(account.getPasswordModifiedAt());

        boolean accountNonExpired = true;

        boolean passwordNonExpired = !currentDateTime.isAfter(passwordModifiedAt
                .plusDays(securityConfigs.getPasswordValidDays()));

        boolean accountNonLock = (account.getPasswordLock() == null)
                || (account.getPasswordLock().getFailureCount() <= securityConfigs
                        .getAuthenticationFailureMaxCount());

        return new CustomUserDetails(account, accountNonExpired, passwordNonExpired, accountNonLock);
    }

}
