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
import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;

@Service("customUserDetailsService")
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final @lombok.NonNull AccountRepository accountRepository;

    private final @lombok.NonNull MessageSource messageSource;

    private final @lombok.NonNull DateFactory dateFactory;

    private final @lombok.NonNull SecurityConfigs securityConfigs;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findOneByAccountId(username);
        if (account == null) {
            throw new UsernameNotFoundException(
                    Message.SECURITY_ACCOUNT_NOT_FOUND.text(messageSource));
        }

        DateTime currentDateTime = dateFactory.newDateTime();

        boolean accountNonExpired = true;

        boolean passwordNonExpired = account.isPasswordNonExpired(currentDateTime,
                securityConfigs.getPasswordValidDays());

        boolean accountNonLock = account.isAccountNonLock(securityConfigs
                .getAuthenticationFailureMaxCount());

        return new CustomUserDetails(account, accountNonExpired, passwordNonExpired, accountNonLock);
    }

}
