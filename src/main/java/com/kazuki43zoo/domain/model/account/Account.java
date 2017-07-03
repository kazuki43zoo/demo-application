package com.kazuki43zoo.domain.model.account;

import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data
@lombok.ToString(exclude = "password")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private String accountId;
    private String password;
    private DateTime passwordModifiedAt;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean enabledAutoLogin;
    private AccountPasswordLock passwordLock;
    private List<AccountAuthority> authorities;
    private List<AccountPasswordHistory> passwordHistories;
    private List<AccountAuthenticationHistory> authenticationHistories;

    public Account addAuthority(final AccountAuthority authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(authority);
        return this;
    }

    public void countUpPasswordFailureCount(final DateTime modifiedAt) {
        final AccountPasswordLock currentPasswordLock = getPasswordLock();
        if (currentPasswordLock == null) {
            setPasswordLock(new AccountPasswordLock(getAccountUuid(), 1, modifiedAt));
        } else {
            currentPasswordLock.countUpFailureCount();
            currentPasswordLock.setModifiedAt(modifiedAt);
        }
    }

    public void resetPasswordFailureCount() {
        setPasswordLock(null);
    }

    public boolean isPastUsedPassword(final String rawPassword, final PasswordEncoder passwordEncoder) {
        if (getPasswordHistories() == null) {
            return false;
        }
        for (final AccountPasswordHistory passwordHistory : getPasswordHistories()) {
            if (passwordEncoder.matches(rawPassword, passwordHistory.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPasswordInitialized() {
        return getPasswordModifiedAt() != null;
    }

    public boolean isPasswordNonExpired(final DateTime currentDateTime, final int passwordValidDays) {
        if(getPasswordModifiedAt() == null){
            return true;
        }
        return !currentDateTime.isAfter(getPasswordModifiedAt().plusDays(passwordValidDays));
    }

    public boolean isAccountNonLock(final int authenticationFailureMaxCount) {
        return (getPasswordLock() == null)
                || (getPasswordLock().getFailureCount() <= authenticationFailureMaxCount);
    }

}
