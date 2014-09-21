package com.github.kazuki43zoo.domain.model.calendar;

import java.io.Serializable;

@lombok.Data
public class HappyMondayHoliday implements Holiday, Serializable {
    private static final long serialVersionUID = 1L;
    private int targetMonth;
    private int targetWeek;
    private String holidayName;
    private String holidayNameJa;
    private int beginYear;
    private int endYear;
}
