package com.kazuki43zoo.web.security;

import com.kazuki43zoo.core.config.SecurityConfigs;
import com.kazuki43zoo.core.message.Message;
import com.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.kazuki43zoo.domain.model.account.AuthenticationType;
import com.kazuki43zoo.domain.service.security.AuthenticationSharedService;
import com.kazuki43zoo.domain.service.security.CustomAuthenticationDetails;
import org.dozer.Mapper;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@lombok.RequiredArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final MessageSource messageSource;

    private final AuthenticationSharedService authenticationSharedService;

    private final SecurityConfigs securityConfigs;

    private final AuthenticationDetailsSource<HttpServletRequest, CustomAuthenticationDetails> authenticationDetailsSource;

    private final Mapper beanMapper;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof SessionAuthenticationException) {
            createConcurrentAuthenticationFailureHistory(request);
        }
        super.onAuthenticationFailure(request, response, exception);
    }

    private void createConcurrentAuthenticationFailureHistory(final HttpServletRequest request) {
        final String accountId = request.getParameter(this.securityConfigs.getUsernameParameter());
        final String message = Message.SECURITY_CONCURRENT_LOGIN.text(this.messageSource);
        final CustomAuthenticationDetails authenticationDetails = this.authenticationDetailsSource.buildDetails(request);
        final AccountAuthenticationHistory authenticationHistory = this.beanMapper.map(authenticationDetails, AccountAuthenticationHistory.class);
        this.authenticationSharedService.createAuthenticationFailureHistory(accountId, authenticationHistory, AuthenticationType.LOGIN, message);
    }

}
