package com.github.kazuki43zoo.core.message;

import org.springframework.context.MessageSource;
import org.terasoluna.gfw.common.message.ResultMessageType;
import org.terasoluna.gfw.common.message.ResultMessages;

import java.util.Locale;

import static org.terasoluna.gfw.common.message.StandardResultMessageType.*;

public enum Message {

    // ---------------
    // for fw
    // ---------------
    // e.demo.fw.5001
    FW_NOT_FOUND(DANGER, "e.demo.fw.5001"),
    // e.demo.fw.5002
    FW_VALID_SESSION_NOT_EXISTS(WARNING, "e.demo.fw.5002"),
    // e.demo.fw.7001
    FW_TRANSACTION_TOKEN_ERROR(DANGER, "e.demo.fw.7001"),
    // e.demo.fw.7002
    FW_VALID_ACCESS_DENIED(DANGER, "e.demo.fw.7002"),
    // e.demo.fw.8001
    FW_BUSINESS_ERROR(DANGER, "e.demo.fw.8001"),
    // e.demo.fw.9001
    FW_SYSTEM_ERROR(DANGER, "e.demo.fw.9001"),
    // e.demo.fw.9002
    FW_DA_ACCESS_ERROR(DANGER, "e.demo.fw.9002"),
    // e.demo.fw.9003
    FW_DATA_INCONSISTENCIES(DANGER, "e.demo.fw.9003"),
    // ---------------
    // for account
    // ---------------
    // i.demo.account.0001
    ACCOUNT_CREATED(SUCCESS, "i.demo.account.0001"),
    // i.demo.account.0002
    ACCOUNT_EDITED(SUCCESS, "i.demo.account.0002"),
    // i.demo.account.0003
    ACCOUNT_DELETED(SUCCESS, "i.demo.account.0003"),
    // i.demo.account.0004
    ACCOUNT_UNLOCKED(SUCCESS, "i.demo.account.0004"),
    // i.demo.account.0005
    ACCOUNT_PROFILE_EDITED(SUCCESS, "i.demo.account.0005"),
    // e.demo.account.2001
    ACCOUNT_ID_USED(DANGER, "e.demo.account.2001"),
    // ---------------
    // for password
    // ---------------
    // i.demo.password.0001
    PASSWORD_CHANGED(SUCCESS, "i.demo.password.0001"),
    // e.demo.password.2001
    PASSWORD_USED_PAST(DANGER, "e.demo.password.2001"),
    // e.demo.password.2002
    PASSWORD_CONTAINS_ACCOUNT_ID(DANGER, "e.demo.password.2002"),
    // ---------------
    // for auth
    // ---------------
    // i.demo.auth.0001
    AUTH_ENCOURAGE_LOGIN(INFO, "i.demo.auth.0001"),
    // i.demo.auth.0002
    AUTH_LOGOUT(SUCCESS, "i.demo.auth.0002"),
    // e.demo.auth.8001
    AUTH_ENCOURAGE_CHANGE_PASSWORD(DANGER, "e.demo.auth.2001"),
    // ---------------
    // for security
    // ---------------
    // e.demo.security.5001
    SECURITY_ACCOUNT_NOT_FOUND(DANGER, "e.demo.security.5001"),
    // e.demo.security.8001
    SECURITY_CONCURRENT_LOGIN(DANGER, "e.demo.security.8001"),
    //
    ;

    private final ResultMessageType type;
    private final String code;
    private final String defaultMessage;

    private Message(ResultMessageType type, String code) {
        this(type, code, null);
    }

    private Message(ResultMessageType type, String code, String defaultMessage) {
        this.type = type;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public ResultMessages resultMessages(Object... args) {
        return new ResultMessages(type).add(code, args);
    }

    public String text(MessageSource messageSource, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, Locale.getDefault());
    }

    public String text(MessageSource messageSource, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    public String code() {
        return code;
    }

}
