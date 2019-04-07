package com.kazuki43zoo.domain.service.security;

import com.github.dozermapper.core.Mapper;
import com.kazuki43zoo.core.message.Message;
import com.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.kazuki43zoo.domain.model.account.AuthenticationType;
import com.kazuki43zoo.domain.service.password.PasswordSharedService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureServiceExceptionEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.SystemException;

@Transactional
@Component
@lombok.RequiredArgsConstructor
public class AuthenticationEventListeners {

    private final AuthenticationSharedService authenticationSharedService;

    private final PasswordSharedService passwordSharedService;

    private final Mapper beanMapper;

    @EventListener
    public void onAuthenticationFailureBadCredentials(final AuthenticationFailureBadCredentialsEvent event) {
        final String failedAccountId = event.getAuthentication().getName();

        this.passwordSharedService.countUpPasswordFailureCount(failedAccountId);

        final AccountAuthenticationHistory authenticationHistory = this.beanMapper.map(event.getAuthentication().getDetails(), AccountAuthenticationHistory.class);
        this.authenticationSharedService.createAuthenticationFailureHistory(failedAccountId, authenticationHistory, AuthenticationType.LOGIN, event.getException().getMessage());
    }

    @EventListener
    public void onInteractiveAuthenticationSuccess(final InteractiveAuthenticationSuccessEvent event) {
        final CustomUserDetails userDetails = CustomUserDetails.getInstance(event.getAuthentication());

        this.passwordSharedService.resetPasswordLock(userDetails.getAccount());

        createAuthenticationSuccessHistory(event, userDetails);
    }

    @EventListener
    public void onAuthenticationFailureServiceException(final AuthenticationFailureServiceExceptionEvent event){
        throw new SystemException(Message.FW_SYSTEM_ERROR.code(), event.getException());
    }

    private void createAuthenticationSuccessHistory(final InteractiveAuthenticationSuccessEvent event, final CustomUserDetails userDetails) {
        final AccountAuthenticationHistory authenticationHistory = this.beanMapper.map(event.getAuthentication().getDetails(), AccountAuthenticationHistory.class);
        final AuthenticationType authenticationType;
        if (event.getAuthentication() instanceof RememberMeAuthenticationToken) {
            authenticationType = AuthenticationType.AUTO_LOGIN;
        } else {
            authenticationType = AuthenticationType.LOGIN;
        }

        this.authenticationSharedService.createAuthenticationSuccessHistory(userDetails.getAccount(), authenticationHistory, authenticationType);
    }

}
