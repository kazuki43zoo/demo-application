package com.kazuki43zoo.web.security;

import com.github.dozermapper.core.Mapper;
import com.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.kazuki43zoo.domain.model.account.AuthenticationType;
import com.kazuki43zoo.domain.service.security.AuthenticationSharedService;
import com.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@lombok.RequiredArgsConstructor
public final class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final AuthenticationSharedService authenticationSharedService;

    private final Mapper beanMapper;

    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        createLogoutSuccessHistory(authentication);
        super.onLogoutSuccess(request, response, authentication);
    }

    private void createLogoutSuccessHistory(final Authentication authentication) {
        final CustomUserDetails userDetails = CustomUserDetails.getInstance(authentication);
        final AccountAuthenticationHistory authenticationHistory = this.beanMapper.map(authentication.getDetails(), AccountAuthenticationHistory.class);
        this.authenticationSharedService.createAuthenticationSuccessHistory(userDetails.getAccount(), authenticationHistory, AuthenticationType.LOGOUT);
    }

}
