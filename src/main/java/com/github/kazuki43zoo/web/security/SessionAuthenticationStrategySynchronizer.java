package com.github.kazuki43zoo.web.security;

import com.github.kazuki43zoo.domain.repository.account.AccountRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;

@Aspect
@Component
public class SessionAuthenticationStrategySynchronizer {

    @Inject
    AccountRepository accountRepository;

    private final TransactionTemplate transactionTemplate;

    @Inject
    public SessionAuthenticationStrategySynchronizer(PlatformTransactionManager transactionManager, AccountRepository accountRepository) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName(getClass().getName());
        this.transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
    }

    @Around("execution( * org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy.onAuthentication(..))")
    public void synchronize(final ProceedingJoinPoint joinPoint) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Authentication authentication = Authentication.class.cast(joinPoint.getArgs()[0]);
                accountRepository.lockByAccountIdWithinTransaction(authentication.getName());
                try {
                    joinPoint.proceed();
                } catch (RuntimeException e) {
                    throw e;
                } catch (Error e) {
                    throw e;
                } catch (Throwable e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

}
