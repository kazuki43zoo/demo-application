package com.kazuki43zoo.domain.service.security;

import com.kazuki43zoo.core.config.SecurityConfigs;
import com.kazuki43zoo.core.message.Message;
import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.repository.account.AccountRepository;
import org.joda.time.DateTime;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import javax.inject.Inject;

@Service("customUserDetailsService")
public final class CustomUserDetailsService implements UserDetailsService {

    @Inject
    AccountRepository accountRepository;

    @Inject
    MessageSource messageSource;

    @Inject
    JodaTimeDateFactory dateFactory;

    @Inject
    SecurityConfigs securityConfigs;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Account account = accountRepository.findOneByAccountId(username);
        if (account == null) {
            throw new UsernameNotFoundException(Message.SECURITY_ACCOUNT_NOT_FOUND.text(messageSource));
        }

        final DateTime currentDateTime = dateFactory.newDateTime();

        final boolean passwordNonExpired = (account.isPasswordInitialized() && account.isPasswordNonExpired(currentDateTime, securityConfigs.getPasswordValidDays()));

        final boolean accountNonLock = account.isAccountNonLock(securityConfigs.getAuthenticationFailureMaxCount());

        return new CustomUserDetails(account, true, passwordNonExpired, accountNonLock);
    }

}
