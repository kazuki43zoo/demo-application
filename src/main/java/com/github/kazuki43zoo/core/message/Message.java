package com.github.kazuki43zoo.core.message;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.terasoluna.gfw.common.message.ResultMessageType;
import org.terasoluna.gfw.common.message.ResultMessages;

import java.util.Locale;

import static org.terasoluna.gfw.common.message.StandardResultMessageType.*;

public enum Message {

    // ---------------
    // for fw
    // ---------------
    FW_NOT_FOUND(DANGER, "e.demo.fw.5001", "not found."),
    FW_VALID_SESSION_NOT_EXISTS(WARNING, "e.demo.fw.5002", "session not exists."),
    FW_TRANSACTION_TOKEN_ERROR(DANGER, "e.demo.fw.7001", "transaction token error."),
    FW_VALID_ACCESS_DENIED(DANGER, "e.demo.fw.7002", "valid access denied."),
    FW_BUSINESS_ERROR(DANGER, "e.demo.fw.8001", "business error."),
    FW_SYSTEM_ERROR(DANGER, "e.demo.fw.9001", "system error."),
    FW_DATA_ACCESS_ERROR(DANGER, "e.demo.fw.9002", "data access error."),
    FW_DATA_INCONSISTENCIES(DANGER, "e.demo.fw.9003", "data inconsistencies."),
    // ---------------
    // for account
    // ---------------
    ACCOUNT_CREATED(SUCCESS, "i.demo.account.0001"),
    ACCOUNT_EDITED(SUCCESS, "i.demo.account.0002"),
    ACCOUNT_DELETED(SUCCESS, "i.demo.account.0003"),
    ACCOUNT_UNLOCKED(SUCCESS, "i.demo.account.0004"),
    ACCOUNT_PROFILE_EDITED(SUCCESS, "i.demo.account.0005"),
    ACCOUNT_ID_USED(DANGER, "e.demo.account.2001"),
    // ---------------
    // for password
    // ---------------
    PASSWORD_CHANGED(SUCCESS, "i.demo.password.0001"),
    PASSWORD_USED_PAST(DANGER, "e.demo.password.2001"),
    PASSWORD_CONTAINS_ACCOUNT_ID(DANGER, "e.demo.password.2002"),
    // ---------------
    // for auth
    // ---------------
    AUTH_ENCOURAGE_LOGIN(INFO, "i.demo.auth.0001"),
    AUTH_LOGOUT(SUCCESS, "i.demo.auth.0002"),
    AUTH_ENCOURAGE_CHANGE_PASSWORD_NOT_INITIALIZED(DANGER, "e.demo.auth.2001"),
    AUTH_ENCOURAGE_CHANGE_PASSWORD_EXPIRED(DANGER, "e.demo.auth.2002"),
    // ---------------
    // for security
    // ---------------
    SECURITY_ACCOUNT_NOT_FOUND(DANGER, "e.demo.security.5001"),
    SECURITY_CONCURRENT_LOGIN(DANGER, "e.demo.security.8001"),
    //
    ;

    private final ResultMessageType type;
    private final String code;
    private final String defaultMessage;

    Message(ResultMessageType type, String code) {
        this(type, code, code);
    }

    Message(ResultMessageType type, String code, String defaultMessage) {
        this.type = type;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public ResultMessages resultMessages(Object... args) {
        return new ResultMessages(type).add(code, args);
    }

    public String text(MessageSource messageSource, Object... args) {
        return text(messageSource, LocaleContextHolder.getLocale(), args);
    }

    public String text(MessageSource messageSource, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    public String code() {
        return code;
    }

}
