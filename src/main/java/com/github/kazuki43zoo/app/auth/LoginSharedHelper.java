package com.github.kazuki43zoo.app.auth;

import com.github.kazuki43zoo.domain.model.account.Account;
import com.github.kazuki43zoo.domain.service.account.AccountSharedService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class LoginSharedHelper {

    @Inject
    AccountSharedService accountSharedService;

    public String generateAuthenticationProcessingUrl(String accountId) {
        Account account = accountSharedService.getAccount(accountId);
        boolean enabledAutoLogin = false;
        if (account != null) {
            enabledAutoLogin = account.isEnabledAutoLogin();
        }
        return "forward:/auth/authenticate?remember_me=" + enabledAutoLogin;

    }

}
