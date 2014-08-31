package com.github.kazuki43zoo.domain.model.calendar;

import java.io.Serializable;

import lombok.Data;

import org.joda.time.LocalDate;

@Data
public class SeasonalHoliday implements Holiday, Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate targetDate;
    private String holidayName;
    private String holidayNameJa;

}
