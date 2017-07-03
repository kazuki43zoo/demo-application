package com.kazuki43zoo.domain.model.calendar;

import java.io.Serializable;

@lombok.Data
public class FixedHoliday implements Holiday, Serializable {
    private static final long serialVersionUID = 1L;
    private int targetMonth;
    private int targetDay;
    private String holidayName;
    private String holidayNameJa;
    private Integer transferredLaterDays;
    private int beginYear;
    private int endYear;

}
