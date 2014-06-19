package com.github.kazuki43zoo.app.common.security;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.service.account.AccountSharedService;

@Transactional
@Component
public class BadCredentialEventListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Inject
    AccountSharedService accountSharedService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String failedAccountId = event.getAuthentication().getName();

        accountSharedService.countUpPasswordFailureCount(failedAccountId);

        AccountAuthenticationHistory authenticationHistory = beanMapper.map(event
                .getAuthentication().getDetails(), AccountAuthenticationHistory.class);
        accountSharedService.createLoginFailureHistory(failedAccountId, authenticationHistory);
    }

}
