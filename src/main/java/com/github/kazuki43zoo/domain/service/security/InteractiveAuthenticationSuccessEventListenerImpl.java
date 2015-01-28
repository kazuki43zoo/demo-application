package com.github.kazuki43zoo.domain.service.security;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.password.PasswordSharedService;
import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Transactional(noRollbackFor = ConcurrentLoginException.class)
@Component
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class InteractiveAuthenticationSuccessEventListenerImpl implements
        ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @lombok.NonNull
    private final AuthenticationService authenticationService;

    @lombok.NonNull
    private final PasswordSharedService passwordSharedService;

    @lombok.NonNull
    private final Mapper beanMapper;

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
