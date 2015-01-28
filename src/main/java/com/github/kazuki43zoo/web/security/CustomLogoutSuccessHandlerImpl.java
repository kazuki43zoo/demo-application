package com.github.kazuki43zoo.web.security;

import com.github.kazuki43zoo.domain.model.account.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.model.account.AuthenticationType;
import com.github.kazuki43zoo.domain.service.security.AuthenticationService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("customLogoutSuccessHandler")
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class CustomLogoutSuccessHandlerImpl extends SimpleUrlLogoutSuccessHandler {

    @lombok.NonNull
    private final AuthenticationService authenticationService;

    @lombok.NonNull
    private final Mapper beanMapper;

    @Value("${CustomLogoutSuccessHandlerImpl.defaultTargetUrl:/auth/logout?success}")
    public void setDefaultTargetUrl(String defaultTargetUrl) {
        super.setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        AccountAuthenticationHistory authenticationHistory = beanMapper.map(
                authentication.getDetails(), AccountAuthenticationHistory.class);

        authenticationService.createAuthenticationSuccessHistory(userDetails.getAccount(),
                authenticationHistory, AuthenticationType.LOGOUT);

        super.onLogoutSuccess(request, response, authentication);
    }

}
