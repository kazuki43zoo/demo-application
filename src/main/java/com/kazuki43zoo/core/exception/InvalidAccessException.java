package com.kazuki43zoo.core.exception;

import com.kazuki43zoo.core.message.Message;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;
import org.terasoluna.gfw.common.message.ResultMessages;

public final class InvalidAccessException extends ResultMessagesNotificationException {

    private static final long serialVersionUID = 1L;

    public InvalidAccessException() {
        super(Message.FW_VALID_ACCESS_DENIED.resultMessages());
    }

    public InvalidAccessException(final ResultMessages messages) {
        super(messages);
    }

}
