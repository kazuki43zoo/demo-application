package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Transactional(noRollbackFor = ConcurrentLoginException.class)
@Component
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class AuthenticationSuccessEventListenerImpl implements
        ApplicationListener<AuthenticationSuccessEvent> {

    @lombok.NonNull
    private final MessageSource messageSource;

    @lombok.NonNull
    private final AuthenticationService authenticationService;

    @lombok.NonNull
    private final Mapper beanMapper;

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
