package com.github.kazuki43zoo.domain.service.timecard;

import org.joda.time.LocalDate;

import com.github.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.github.kazuki43zoo.domain.model.timecard.TimeCard;

public interface TimeCardService {

    TimeCard getTimeCard(String accountUuid, LocalDate targetMonth);

    TimeCard getDefaultTimeCard(String accountUuid, LocalDate targetMonth);

    void saveTimeCard(String accountUuid, LocalDate targetMonth, TimeCard timeCard);

    DailyAttendance getDailyAttendance(String accountUuid, LocalDate targetDate);

    void saveDailyAttendance(String accountUuid, LocalDate targetDate, DailyAttendance attendance);

}
