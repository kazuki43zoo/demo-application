package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.AuthenticationType;
import com.github.kazuki43zoo.domain.service.password.PasswordSharedService;

@Transactional(dontRollbackOn = ConcurrentLoginException.class)
@Named
public class InteractiveAuthenticationSuccessEventListenerImpl implements
        ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Inject
    AuthenticationService authenticationService;

    @Inject
    PasswordSharedService passwordSharedService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        CustomUserDetails userDetails = (CustomUserDetails) event.getAuthentication()
                .getPrincipal();
        CustomAuthenticationDetails authenticationDetails = (CustomAuthenticationDetails) event
                .getAuthentication().getDetails();

        AccountAuthenticationHistory authenticationHistory = beanMapper.map(authenticationDetails,
                AccountAuthenticationHistory.class);

        passwordSharedService.resetPasswordLock(userDetails.getAccount());
        authenticationService.createAuthenticationSuccessHistory(userDetails.getAccount(),
                authenticationHistory, AuthenticationType.login);
    }

}
