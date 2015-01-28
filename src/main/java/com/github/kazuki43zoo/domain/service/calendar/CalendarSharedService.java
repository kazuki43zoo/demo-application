package com.github.kazuki43zoo.domain.service.calendar;

import com.github.kazuki43zoo.domain.model.calendar.Holiday;
import org.joda.time.LocalDate;

import java.util.Map;

public interface CalendarSharedService {
    public Map<LocalDate, Holiday> getHolodaies(LocalDate month);
}
