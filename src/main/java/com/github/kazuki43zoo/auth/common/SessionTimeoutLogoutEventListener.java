package com.github.kazuki43zoo.auth.common;

import java.util.List;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.kazuki43zoo.domain.model.AccountAuthenticationHistory;
import com.github.kazuki43zoo.domain.service.account.AccountSharedService;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;

@Transactional
@Component
public class SessionTimeoutLogoutEventListener implements
        ApplicationListener<HttpSessionDestroyedEvent> {

    @Inject
    AccountSharedService accountSharedService;

    @Inject
    Mapper beanMapper;

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        List<SecurityContext> securityContexts = event.getSecurityContexts();
        for (SecurityContext securityContext : securityContexts) {
            CustomUserDetails userDetails = (CustomUserDetails) securityContext.getAuthentication()
                    .getPrincipal();
            AccountAuthenticationHistory authenticationHistory = beanMapper.map(securityContext
                    .getAuthentication().getDetails(), AccountAuthenticationHistory.class);
            accountSharedService.createSessionTimeoutHistory(userDetails.getAccount(),
                    authenticationHistory);
        }
    }

}
