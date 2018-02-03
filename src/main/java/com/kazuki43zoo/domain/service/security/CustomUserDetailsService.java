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

@Service("customUserDetailsService")
@lombok.RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final MessageSource messageSource;

    private final JodaTimeDateFactory dateFactory;

    private final SecurityConfigs securityConfigs;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Account account = this.accountRepository.findOneByAccountId(username);
        if (account == null) {
            throw new UsernameNotFoundException(Message.SECURITY_ACCOUNT_NOT_FOUND.text(messageSource));
        }

        final DateTime currentDateTime = this.dateFactory.newDateTime();

        final boolean passwordNonExpired = (account.isPasswordInitialized() && account.isPasswordNonExpired(currentDateTime, securityConfigs.getPasswordValidDays()));

        final boolean accountNonLock = account.isAccountNonLock(this.securityConfigs.getAuthenticationFailureMaxCount());

        return new CustomUserDetails(account, true, passwordNonExpired, accountNonLock);
    }

}
