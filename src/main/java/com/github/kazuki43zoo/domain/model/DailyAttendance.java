package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DailyAttendance implements Serializable {
    public static final LocalTime BASE_BEGIN_TIME = LocalTime.parse("9:00");
    public static final LocalTime BASE_FINISH_TIME = LocalTime.parse("17:45");
    private static final LocalTime MIDNIGHT_BEGIN_TIME = LocalTime.parse("22:00");
    public static final int DEFAULT_ACTUAL_WORKING_MINUTE = Long.valueOf(
            TimeUnit.HOURS.toMinutes(7) + TimeUnit.MINUTES.toMinutes(45)).intValue();
    private static final long serialVersionUID = 1L;

    private String accountUuid;
    private LocalDate targetDate;
    private LocalTime beginTime;
    private LocalTime finishTime;
    private boolean paidLeave;
    private String specialWorkCode;
    private String note;
    private WorkPlace workPlace;

    @Getter
    transient private int actualWorkingMinute;
    @Getter
    transient private int compensationMinute;
    @Getter
    transient private int midnightWorkingMinute;
    @Getter
    transient private boolean tardyOrEarlyLeaving;
    @Getter
    transient private boolean absence;

    public void calculate(WorkPlace defaultWorkPlace) {
        clearCalculate();
        if (beginTime != null && finishTime != null) {
            this.actualWorkingMinute = calculateIntervalMinute(beginTime, finishTime);
            if (actualWorkingMinute < DEFAULT_ACTUAL_WORKING_MINUTE) {
                this.compensationMinute = DEFAULT_ACTUAL_WORKING_MINUTE - actualWorkingMinute;
            }
            if (finishTime.isAfter(MIDNIGHT_BEGIN_TIME)) {
                this.midnightWorkingMinute = calculateIntervalMinute(MIDNIGHT_BEGIN_TIME,
                        finishTime);
            }
        } else if (beginTime == null && finishTime == null) {
            if (isWorkDay()) {
                this.absence = true;
            }
        }
        if ((beginTime != null && beginTime.isAfter(BASE_BEGIN_TIME))
                || (finishTime != null && finishTime.isBefore(BASE_FINISH_TIME))) {
            this.tardyOrEarlyLeaving = true;
        }
        if (!tardyOrEarlyLeaving) {
            this.specialWorkCode = null;
        }
    }

    public void setDefault(WorkPlace defaultWorkPlace) {
        setBeginTime(DailyAttendance.BASE_BEGIN_TIME);
        setFinishTime(DailyAttendance.BASE_FINISH_TIME);
    }

    public boolean isWorkDay() {
        if (targetDate == null) {
            return false;
        }
        return !(targetDate.getDayOfWeek() == DateTimeConstants.SATURDAY || targetDate
                .getDayOfWeek() == DateTimeConstants.SUNDAY);
    }

    private void clearCalculate() {
        this.actualWorkingMinute = 0;
        this.compensationMinute = 0;
        this.midnightWorkingMinute = 0;
        this.tardyOrEarlyLeaving = false;
        this.absence = false;
    }

    private int calculateIntervalMinute(LocalTime begin, LocalTime finish) {
        return Long.valueOf(
                TimeUnit.MILLISECONDS.toMinutes(new Interval(begin.toDateTimeToday(), finish
                        .toDateTimeToday()).toDuration().getMillis())).intValue();
    }

}
