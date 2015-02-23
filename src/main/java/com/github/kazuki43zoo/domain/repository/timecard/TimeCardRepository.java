package com.github.kazuki43zoo.domain.repository.timecard;

import com.github.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.github.kazuki43zoo.domain.model.timecard.TimeCard;
import org.apache.ibatis.annotations.Param;
import org.joda.time.LocalDate;

public interface TimeCardRepository {

    TimeCard findOne(
            @Param("accountUuid") String accountUuid,
            @Param("targetMonth") LocalDate targetMonth);

    boolean create(TimeCard timeCard);

    boolean update(TimeCard timeCard);

    boolean delete(
            @Param("accountUuid") String accountUuid,
            @Param("targetMonth") LocalDate targetMonth);

    DailyAttendance findOneDailyAttendance(
            @Param("accountUuid") String accountUuid,
            @Param("targetDate") LocalDate targetDate);

    boolean createDailyAttendance(DailyAttendance dailyAttendance);

    boolean updateDailyAttendance(DailyAttendance dailyAttendance);

}
