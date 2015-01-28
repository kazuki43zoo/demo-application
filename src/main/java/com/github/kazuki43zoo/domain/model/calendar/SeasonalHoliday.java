package com.github.kazuki43zoo.domain.model.calendar;

import org.joda.time.LocalDate;

import java.io.Serializable;

@lombok.Data
public class SeasonalHoliday implements Holiday, Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate targetDate;
    private String holidayName;
    private String holidayNameJa;

}
