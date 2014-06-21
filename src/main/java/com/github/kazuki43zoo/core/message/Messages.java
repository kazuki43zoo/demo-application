package com.github.kazuki43zoo.core.message;

import static org.terasoluna.gfw.common.message.StandardResultMessageType.DANGER;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.SUCCESS;
import static org.terasoluna.gfw.common.message.StandardResultMessageType.INFO;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.terasoluna.gfw.common.message.ResultMessageType;
import org.terasoluna.gfw.common.message.ResultMessages;

public enum Messages {

    // e.demo.fw.5001
    FW_NOT_FOUND(DANGER, "e.demo.fw.5001"),
    // e.demo.fw.5002
    FW_VALID_SESSION_NOT_EXISTS(DANGER, "e.demo.fw.5002"),
    // e.demo.fw.7001
    FW_TRANSACTION_TOKEN_ERROR(DANGER, "e.demo.fw.7001"),
    // e.demo.fw.8001
    FW_BUSINESS_ERROR(DANGER, "e.demo.fw.8001"),
    // e.demo.fw.9001
    FW_SYSTEM_ERROR(DANGER, "e.demo.fw.9001"),
    // e.demo.fw.9002
    FW_DA_ACCESS_ERROR(DANGER, "e.demo.fw.9002"),
    // e.demo.fw.7002
    FW_VALID_ACCESS_DENIED(DANGER, "e.demo.fw.7002"),
    // i.demo.account.0001
    ACCOUNT_CREATED(SUCCESS, "i.demo.account.0001"),
    // i.demo.account.0002
    ACCOUNT_EDITED(SUCCESS, "i.demo.account.0002"),
    // i.demo.account.0003
    ACCOUNT_DELETED(SUCCESS, "i.demo.account.0003"),
    // i.demo.account.0004
    ACCOUNT_PROFILE_EDITED(SUCCESS, "i.demo.account.0004"),
    // e.demo.account.2001
    ACCOUNT_ID_USED(DANGER, "e.demo.account.2001"),
    // e.demo.account.2002
    ACCOUNT_PASSWORD_USED_PAST(DANGER, "e.demo.account.2002"),
    // i.demo.auth.0001
    AUTH_ENCOURAGE_LOGIN(INFO, "i.demo.auth.0001"),
    // i.demo.auth.0002
    AUTH_LOGOUT(SUCCESS, "i.demo.auth.0002"),
    // e.demo.security.8001
    SECURITY_ACCOUNT_NOT_FOUND(DANGER, "e.demo.security.8001"),
    // e.demo.security.8002
    SECURITY_CONCURRENT_LOGIN(DANGER, "e.demo.security.8002"),
    //
    ;

    private final ResultMessageType type;
    private final String code;

    private Messages(ResultMessageType type, String code) {
        this.type = type;
        this.code = code;
    }

    public ResultMessages buildResultMessages(Object... args) {
        return new ResultMessages(type).add(code, args);
    }

    public String buildMessage(MessageSource messageSource, Object... args) {
        return messageSource.getMessage(code, args, null, Locale.getDefault());
    }

}
