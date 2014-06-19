package com.github.kazuki43zoo.domain.service.security;

import org.springframework.security.authentication.AccountStatusException;

public class ConcurrentLoginException extends AccountStatusException {

    private static final long serialVersionUID = 1L;

    public ConcurrentLoginException(String msg) {
        super(msg);
    }

}
