package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.model.account.AccountAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public final class CustomUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final Account account;

    public CustomUserDetails(
            Account account,
            boolean accountNonExpired,
            boolean passwordNonExpired,
            boolean accountNonLock) {
        super(account.getAccountId(), account.getPassword(), account.isEnabled(), accountNonExpired, passwordNonExpired, accountNonLock, toGrantedAuthorities(account));
        this.account = account;
    }

    private static Collection<GrantedAuthority> toGrantedAuthorities(Account account) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (AccountAuthority authority : account.getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getAuthority()));
        }
        return grantedAuthorities;
    }

    public Account getAccount() {
        return account;
    }

    public static CustomUserDetails getInstance(Authentication authentication) {
        return (CustomUserDetails) authentication.getPrincipal();
    }

}
