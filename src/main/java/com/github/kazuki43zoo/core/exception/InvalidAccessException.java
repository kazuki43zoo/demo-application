package com.github.kazuki43zoo.core.exception;

import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.common.message.ResultMessages;

import com.github.kazuki43zoo.core.message.Message;

public final class InvalidAccessException extends ResultMessagesNotificationException {

    private static final long serialVersionUID = 1L;

    public InvalidAccessException() {
        super(Message.FW_VALID_ACCESS_DENIED.resultMessages());
    }

    public InvalidAccessException(ResultMessages messages) {
        super(messages);
    }

}
