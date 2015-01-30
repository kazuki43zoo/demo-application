package com.github.kazuki43zoo.infra.dozer.converter;

import org.joda.time.DateTime;

public final class DateTimeConverter extends ImmutableObjectCopyConverter<DateTime, DateTime> {

    public DateTimeConverter() {
        super(DateTime.class);
    }

}
