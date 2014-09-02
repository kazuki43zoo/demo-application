package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.password.PasswordSharedService;

@Transactional(dontRollbackOn = ConcurrentLoginException.class)
@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InteractiveAuthenticationSuccessEventListenerImpl implements
        ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final @NonNull AuthenticationService authenticationService;

    private final @NonNull PasswordSharedService passwordSharedService;

    private final @NonNull Mapper beanMapper;

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
                authenticationHistory, AuthenticationType.LOGIN);
    }

}
