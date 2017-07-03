package com.kazuki43zoo.infra.dozer.converter;

import org.joda.time.LocalDate;

public final class LocalDateConverter extends ImmutableObjectCopyConverter<LocalDate, LocalDate> {

    public LocalDateConverter() {
        super(LocalDate.class);
    }


}
