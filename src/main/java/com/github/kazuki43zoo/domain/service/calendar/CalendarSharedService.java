package com.github.kazuki43zoo.domain.service.calendar;

import java.util.Map;

import org.joda.time.LocalDate;

import com.github.kazuki43zoo.domain.model.calendar.Holiday;

public interface CalendarSharedService {
    public Map<LocalDate, Holiday> getHolodaies(LocalDate month);
}
