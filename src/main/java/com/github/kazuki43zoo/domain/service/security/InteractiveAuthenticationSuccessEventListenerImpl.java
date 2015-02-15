package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.password.PasswordSharedService;
import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Transactional
@Component
public final class InteractiveAuthenticationSuccessEventListenerImpl implements
        ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Inject
    AuthenticationService authenticationService;

    @Inject
    PasswordSharedService passwordSharedService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        CustomUserDetails userDetails = CustomUserDetails.getInstance(event.getAuthentication());

        passwordSharedService.resetPasswordLock(userDetails.getAccount());

        AccountAuthenticationHistory authenticationHistory =
                beanMapper.map(event.getAuthentication().getDetails(), AccountAuthenticationHistory.class);
        AuthenticationType authenticationType;
        if (event.getAuthentication() instanceof RememberMeAuthenticationToken) {
            authenticationType = AuthenticationType.AUTO_LOGIN;
        } else {
            authenticationType = AuthenticationType.LOGIN;
        }
        authenticationService.createAuthenticationSuccessHistory(userDetails.getAccount(),
                authenticationHistory, authenticationType);
    }

}
