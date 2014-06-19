package com.github.kazuki43zoo.app.common.security;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.service.account.AccountSharedService;
import com.github.kazuki43zoo.domain.service.security.ConcurrentLoginException;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;

@Transactional
@Component
public class AthenticationSuccessEventListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    @Inject
    MessageSource messageSource;

    @Inject
    AccountSharedService accountSharedService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        CustomUserDetails userDetails = (CustomUserDetails) event.getAuthentication()
                .getPrincipal();
        CustomWebAuthenticationDetails authenticationDetails = (CustomWebAuthenticationDetails) event
                .getAuthentication().getDetails();
        if (accountSharedService.isLogin(userDetails.getAccount(),
                authenticationDetails.getSessionId())) {
            throw new ConcurrentLoginException(messageSource.getMessage("e.xx.sec.8002",
                    new String[] { userDetails.getAccount().getAccountId() },
                    "Already has login by another session.", null));
        }

        accountSharedService.clearPasswordLock(userDetails.getAccount());

        AccountAuthenticationHistory authenticationHistory = beanMapper.map(authenticationDetails,
                AccountAuthenticationHistory.class);
        accountSharedService.createLoginSuccessHistory(userDetails.getAccount(),
                authenticationHistory);
    }
}
