package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.core.config.SecurityConfigs;
import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;
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
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class CustomUserDetailsServiceImpl implements UserDetailsService {

    @lombok.NonNull
    private final AccountRepository accountRepository;

    @lombok.NonNull
    private final MessageSource messageSource;

    @lombok.NonNull
    private final JodaTimeDateFactory dateFactory;

    @lombok.NonNull
    private final SecurityConfigs securityConfigs;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findOneByAccountId(username);
        if (account == null) {
            throw new UsernameNotFoundException(
                    Message.SECURITY_ACCOUNT_NOT_FOUND.text(messageSource));
        }

        DateTime currentDateTime = dateFactory.newDateTime();

        boolean passwordNonExpired = account.isPasswordNonExpired(currentDateTime,
                securityConfigs.getPasswordValidDays());

        boolean accountNonLock = account.isAccountNonLock(securityConfigs
                .getAuthenticationFailureMaxCount());

        return new CustomUserDetails(account, true, passwordNonExpired, accountNonLock);
    }

}
