package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.kazuki43zoo.domain.model.Account;
import com.github.kazuki43zoo.domain.repository.account.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Inject
    AccountRepository accountRepository;

    @Inject
    MessageSource messageSource;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findOneByAccountId(username);
        if (account == null) {
            throw new UsernameNotFoundException(messageSource.getMessage("e.xx.sec.0001",
                    new String[] { username }, "Account not found.", null));
        }
        return new CustomUserDetails(account);
    }

}
