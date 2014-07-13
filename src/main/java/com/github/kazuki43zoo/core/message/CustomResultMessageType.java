package com.github.kazuki43zoo.core.message;

import org.terasoluna.gfw.common.message.ResultMessageType;

public enum CustomResultMessageType implements ResultMessageType {

    WARNING("warning");

    private final String type;

    private CustomResultMessageType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
