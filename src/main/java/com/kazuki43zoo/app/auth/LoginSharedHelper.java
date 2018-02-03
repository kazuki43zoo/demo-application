package com.kazuki43zoo.app.auth;

import com.kazuki43zoo.domain.model.account.Account;
import com.kazuki43zoo.domain.service.account.AccountSharedService;
import org.springframework.stereotype.Component;

@Component
@lombok.RequiredArgsConstructor
public class LoginSharedHelper {

    private final AccountSharedService accountSharedService;

    public String generateAuthenticationProcessingUrl(final String username) {
        final Account account = this.accountSharedService.getAccount(username);
        boolean enabledAutoLogin = false;
        if (account != null) {
            enabledAutoLogin = account.isEnabledAutoLogin();
        }
        return "forward:/app/auth/authenticate?remember-me=" + enabledAutoLogin;

    }

}
