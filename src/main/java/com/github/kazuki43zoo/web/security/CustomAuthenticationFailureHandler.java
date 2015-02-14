package com.github.kazuki43zoo.web.security;

import com.github.kazuki43zoo.core.config.SecurityConfigs;
import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.security.AuthenticationService;
import com.github.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;
import org.dozer.Mapper;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.terasoluna.gfw.common.exception.SystemException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    @Inject
    MessageSource messageSource;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    SecurityConfigs securityConfigs;

    @Inject
    AuthenticationDetailsSource<HttpServletRequest, CustomAuthenticationDetails> authenticationDetailsSource;

    @Inject
    Mapper beanMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof AuthenticationServiceException) {
            throw new SystemException(Message.FW_SYSTEM_ERROR.code(), exception);
        }
        if (exception instanceof SessionAuthenticationException) {
            String accountId = request.getParameter(securityConfigs.getUsernameParameter());
            String message = Message.SECURITY_CONCURRENT_LOGIN.text(messageSource);
            CustomAuthenticationDetails authenticationDetails = authenticationDetailsSource.buildDetails(request);
            AccountAuthenticationHistory authenticationHistory = beanMapper.map(
                    authenticationDetails, AccountAuthenticationHistory.class);
            authenticationService.createAuthenticationFailureHistory(accountId, authenticationHistory, AuthenticationType.LOGIN, message);
        }
        super.onAuthenticationFailure(request, response, exception);
    }


}
