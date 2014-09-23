package com.github.kazuki43zoo.infra.dozer.converter;

import org.dozer.DozerConverter;
import org.joda.time.LocalTime;

public final class LocalTimeConverter extends DozerConverter<LocalTime, LocalTime> {

    public LocalTimeConverter() {
        super(LocalTime.class, LocalTime.class);
    }

    @Override
    public LocalTime convertTo(LocalTime source, LocalTime destination) {
        return source;
    }

    @Override
    public LocalTime convertFrom(LocalTime source, LocalTime destination) {
        return source;
    }

}
