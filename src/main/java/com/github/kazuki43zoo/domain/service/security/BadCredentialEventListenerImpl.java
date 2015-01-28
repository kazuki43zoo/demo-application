package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.password.PasswordSharedService;
import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Transactional
@Component
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class BadCredentialEventListenerImpl implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @lombok.NonNull
    private final AuthenticationService authenticationService;

    @lombok.NonNull
    private final PasswordSharedService passwordSharedService;

    @lombok.NonNull
    private final Mapper beanMapper;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String failedAccountId = event.getAuthentication().getName();

        passwordSharedService.countUpPasswordFailureCount(failedAccountId);

        AccountAuthenticationHistory authenticationHistory = beanMapper.map(event
                .getAuthentication().getDetails(), AccountAuthenticationHistory.class);
        authenticationService.createAuthenticationFailureHistory(failedAccountId,
                authenticationHistory, AuthenticationType.LOGIN, event.getException().getMessage());
    }

}
