package com.github.kazuki43zoo.web.security;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.security.AuthenticationSharedService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.dozer.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Inject
    AuthenticationSharedService authenticationSharedService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        createLogoutSuccessHistory(authentication);
        super.onLogoutSuccess(request, response, authentication);
    }

    private void createLogoutSuccessHistory(final Authentication authentication) {
        final CustomUserDetails userDetails = CustomUserDetails.getInstance(authentication);
        final AccountAuthenticationHistory authenticationHistory = beanMapper.map(authentication.getDetails(), AccountAuthenticationHistory.class);
        authenticationSharedService.createAuthenticationSuccessHistory(userDetails.getAccount(), authenticationHistory, AuthenticationType.LOGOUT);
    }

}
