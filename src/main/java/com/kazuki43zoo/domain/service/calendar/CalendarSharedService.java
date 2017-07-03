package com.kazuki43zoo.domain.service.calendar;

import com.kazuki43zoo.domain.model.calendar.Holiday;
import org.joda.time.LocalDate;

import java.util.Map;

public interface CalendarSharedService {
    public Map<LocalDate, Holiday> getHolidays(LocalDate month);
}
