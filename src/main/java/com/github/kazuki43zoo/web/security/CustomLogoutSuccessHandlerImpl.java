package com.github.kazuki43zoo.web.security;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.security.AuthenticationService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.dozer.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class CustomLogoutSuccessHandlerImpl extends SimpleUrlLogoutSuccessHandler {

    @Inject
    AuthenticationService authenticationService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        createLogoutSuccessHistory(authentication);

        super.onLogoutSuccess(request, response, authentication);
    }

    private void createLogoutSuccessHistory(Authentication authentication) {
        CustomUserDetails userDetails = CustomUserDetails.getInstance(authentication);

        AccountAuthenticationHistory authenticationHistory =
                beanMapper.map(authentication.getDetails(), AccountAuthenticationHistory.class);

        authenticationService.createAuthenticationSuccessHistory(userDetails.getAccount(),
                authenticationHistory, AuthenticationType.LOGOUT);
    }

}
