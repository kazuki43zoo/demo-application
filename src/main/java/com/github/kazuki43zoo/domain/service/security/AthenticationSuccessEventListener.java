package com.github.kazuki43zoo.domain.service.security;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.service.account.AccountSharedService;
import com.github.kazuki43zoo.domain.service.account.PasswordSharedService;

@Transactional(noRollbackFor = ConcurrentLoginException.class)
@Component
public class AthenticationSuccessEventListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    @Inject
    MessageSource messageSource;

    @Inject
    AccountSharedService accountSharedService;

    @Inject
    PasswordSharedService passwordSharedService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        CustomUserDetails userDetails = (CustomUserDetails) event.getAuthentication()
                .getPrincipal();
        CustomAuthenticationDetails authenticationDetails = (CustomAuthenticationDetails) event
                .getAuthentication().getDetails();

        AccountAuthenticationHistory authenticationHistory = beanMapper.map(authenticationDetails,
                AccountAuthenticationHistory.class);

        if (accountSharedService.isLogin(userDetails.getAccount(),
                authenticationDetails.getSessionId())) {
            String message = Message.SECURITY_CONCURRENT_LOGIN.buildMessage(messageSource);
            accountSharedService.createLoginFailureHistory(userDetails.getAccount().getAccountId(),
                    authenticationHistory, message);
            throw new ConcurrentLoginException(message);
        }

        passwordSharedService.clearPasswordLock(userDetails.getAccount());

        accountSharedService.createLoginSuccessHistory(userDetails.getAccount(),
                authenticationHistory);
    }
}
