package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.service.password.PasswordSharedService;

@Transactional
@Component
public class BadCredentialEventListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Inject
    AuthenticationService authenticationService;

    @Inject
    PasswordSharedService passwordSharedService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String failedAccountId = event.getAuthentication().getName();

        passwordSharedService.countUpPasswordFailureCount(failedAccountId);

        AccountAuthenticationHistory authenticationHistory = beanMapper.map(event
                .getAuthentication().getDetails(), AccountAuthenticationHistory.class);
        authenticationService.createLoginFailureHistory(failedAccountId, authenticationHistory,
                event.getException().getMessage());
    }

}
