package com.github.kazuki43zoo.domain.model.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private String accountId;
    private String password;
    private DateTime passwordModifiedAt;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private AccountPasswordLock passwordLock;
    private List<AccountAuthority> authorities;
    private List<AccountPasswordHistory> passwordHistories;
    private List<AccountAuthenticationHistory> authenticationHistories;

    public Account addAuthority(AccountAuthority authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(authority);
        return this;
    }

    public void countUpPasswordFailureCount(DateTime modifiedAt) {
        AccountPasswordLock currentPasswordLock = getPasswordLock();
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

    public boolean isPastUsedPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (getPasswordHistories() == null) {
            return false;
        }
        for (AccountPasswordHistory passwordHistory : getPasswordHistories()) {
            if (passwordEncoder.matches(rawPassword, passwordHistory.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPasswordNonExpired(DateTime currentDateTime, int passwordValidDays) {
        return !currentDateTime.isAfter(getPasswordModifiedAt().plusDays(passwordValidDays));
    }

    public boolean isAccountNonLock(int authenticationFailureMaxCount) {
        return (getPasswordLock() == null)
                || (getPasswordLock().getFailureCount() <= authenticationFailureMaxCount);
    }

}
