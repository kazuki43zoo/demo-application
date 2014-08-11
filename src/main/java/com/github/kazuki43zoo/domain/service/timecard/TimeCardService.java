package com.github.kazuki43zoo.domain.service.timecard;

import org.joda.time.LocalDate;

import com.github.kazuki43zoo.domain.model.DailyAttendance;
import com.github.kazuki43zoo.domain.model.TimeCard;
import com.github.kazuki43zoo.domain.model.WorkPlace;

public interface TimeCardService {

    TimeCard getTimeCard(String accountUuid, LocalDate targetMonth);

    TimeCard getDefaultTimeCard(String accountUuid, LocalDate targetMonth);

    void saveTimeCard(String accountUuid, LocalDate targetMonth, TimeCard timeCard);

    void saveDailyAttendance(String accountUuid, LocalDate targetDate, DailyAttendance attendance);

    WorkPlace getWorkPlace(String workPlaceUuid);

    WorkPlace getWorkPlaceDetail(WorkPlace workPlace);

}
