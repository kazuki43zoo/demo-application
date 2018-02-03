package com.kazuki43zoo.domain.repository.timecard;

import com.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.kazuki43zoo.domain.model.timecard.TimeCard;
import org.apache.ibatis.annotations.Param;
import org.joda.time.LocalDate;

public interface TimeCardRepository {

    TimeCard findOne(
            @Param("accountUuid") String accountUuid,
            @Param("targetMonth") LocalDate targetMonth);

    void create(TimeCard timeCard);

    void update(TimeCard timeCard);

    void delete(
            @Param("accountUuid") String accountUuid,
            @Param("targetMonth") LocalDate targetMonth);

    DailyAttendance findOneDailyAttendance(
            @Param("accountUuid") String accountUuid,
            @Param("targetDate") LocalDate targetDate);

    void createDailyAttendance(DailyAttendance dailyAttendance);

    void updateDailyAttendance(DailyAttendance dailyAttendance);

}
