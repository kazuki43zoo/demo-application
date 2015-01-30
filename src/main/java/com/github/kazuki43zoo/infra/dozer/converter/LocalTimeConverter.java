package com.github.kazuki43zoo.infra.dozer.converter;

import org.joda.time.LocalTime;

public final class LocalTimeConverter extends ImmutableObjectCopyConverter<LocalTime, LocalTime> {

    public LocalTimeConverter() {
        super(LocalTime.class);
    }

}
