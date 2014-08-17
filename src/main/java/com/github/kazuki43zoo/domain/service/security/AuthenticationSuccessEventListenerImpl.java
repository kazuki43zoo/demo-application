package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;

@Transactional(dontRollbackOn = ConcurrentLoginException.class)
@Component
public class AuthenticationSuccessEventListenerImpl implements
        ApplicationListener<AuthenticationSuccessEvent> {

    @Inject
    MessageSource messageSource;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        CustomUserDetails userDetails = (CustomUserDetails) event.getAuthentication()
                .getPrincipal();
        CustomAuthenticationDetails authenticationDetails = (CustomAuthenticationDetails) event
                .getAuthentication().getDetails();
        if (authenticationService.isLogin(userDetails.getAccount())) {
            String message = Message.SECURITY_CONCURRENT_LOGIN.text(messageSource);
            AccountAuthenticationHistory authenticationHistory = beanMapper.map(
                    authenticationDetails, AccountAuthenticationHistory.class);
            authenticationService.createAuthenticationFailureHistory(userDetails.getAccount()
                    .getAccountId(), authenticationHistory, AuthenticationType.LOGIN, message);
            throw new ConcurrentLoginException(message);
        }
    }

}
