package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.password.PasswordSharedService;
import org.dozer.Mapper;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Transactional
@Component
public class AuthenticationEventListeners {

    @Inject
    AuthenticationSharedService authenticationSharedService;

    @Inject
    PasswordSharedService passwordSharedService;

    @Inject
    Mapper beanMapper;

    @EventListener
    public void onAuthenticationFailureBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
        String failedAccountId = event.getAuthentication().getName();

        passwordSharedService.countUpPasswordFailureCount(failedAccountId);

        AccountAuthenticationHistory authenticationHistory =
                beanMapper.map(event.getAuthentication().getDetails(), AccountAuthenticationHistory.class);
        authenticationSharedService.createAuthenticationFailureHistory(
                failedAccountId, authenticationHistory, AuthenticationType.LOGIN, event.getException().getMessage());
    }

    @EventListener
    public void onInteractiveAuthenticationSuccess(InteractiveAuthenticationSuccessEvent event) {
        CustomUserDetails userDetails = CustomUserDetails.getInstance(event.getAuthentication());

        passwordSharedService.resetPasswordLock(userDetails.getAccount());

        createAuthenticationSuccessHistory(event, userDetails);
    }

    private void createAuthenticationSuccessHistory(InteractiveAuthenticationSuccessEvent event, CustomUserDetails userDetails) {
        AccountAuthenticationHistory authenticationHistory =
                beanMapper.map(event.getAuthentication().getDetails(), AccountAuthenticationHistory.class);
        AuthenticationType authenticationType;
        if (event.getAuthentication() instanceof RememberMeAuthenticationToken) {
            authenticationType = AuthenticationType.AUTO_LOGIN;
        } else {
            authenticationType = AuthenticationType.LOGIN;
        }

        authenticationSharedService.createAuthenticationSuccessHistory(
                userDetails.getAccount(), authenticationHistory, authenticationType);
    }

}
