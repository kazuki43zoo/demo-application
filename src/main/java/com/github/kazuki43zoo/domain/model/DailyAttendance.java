package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DailyAttendance implements Serializable {

    private static final LocalTime MIDNIGHT_BEGIN_TIME = LocalTime.parse("22:00");
    private static final LocalTime MIDNIGHT_FINISH_TIME = LocalTime.parse("05:00");
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

    public void calculate() {
        calculate(null);
    }

    public void calculate(WorkPlace defaultWorkPlace) {

        clearCalculate();

        WorkPlace actualWorkPlace = workPlace;
        if (actualWorkPlace == null) {
            actualWorkPlace = defaultWorkPlace;
        }

        if (beginTime != null && finishTime != null) {

            // calculate work time interval
            if (targetDate == null) {
                targetDate = LocalDate.now();
            }
            DateTime beginDateTime = targetDate.toDateTime(beginTime);
            DateTime finishDateTime = targetDate.toDateTime(finishTime);
            if (!beginTime.isBefore(finishTime)) {
                finishDateTime = finishDateTime.plusDays(1);
            }
            Interval workTimeInterval = new Interval(beginDateTime, finishDateTime);

            // calculate working minute
            this.actualWorkingMinute = calculateIntervalMinute(workTimeInterval);
            this.actualWorkingMinute -= (this.actualWorkingMinute % actualWorkPlace.getUnitTime()
                    .getMinuteOfHour());

            // calculate compensation minute
            if (actualWorkingMinute < DEFAULT_ACTUAL_WORKING_MINUTE) {
                this.compensationMinute = DEFAULT_ACTUAL_WORKING_MINUTE - actualWorkingMinute;
            }

            // calculate midnight working minute
            List<Interval> midnightIntervals = new ArrayList<>();
            // 00:00-05:00
            midnightIntervals.add(new Interval(targetDate.toDateTimeAtStartOfDay(), targetDate
                    .toDateTime(MIDNIGHT_FINISH_TIME)));
            // 22:00-05:00(29:00)
            midnightIntervals.add(new Interval(targetDate.toDateTime(MIDNIGHT_BEGIN_TIME),
                    targetDate.toDateTime(MIDNIGHT_FINISH_TIME).plusDays(1)));
            // 22:00(46:00)-00:00(48:00)
            midnightIntervals.add(new Interval(targetDate.toDateTime(MIDNIGHT_BEGIN_TIME).plusDays(
                    1), targetDate.toDateTime(MIDNIGHT_BEGIN_TIME).plusDays(1).plusHours(2)));
            for (Interval midnightInterval : midnightIntervals) {
                if (workTimeInterval.overlaps(midnightInterval)) {
                    this.midnightWorkingMinute += calculateIntervalMinute(workTimeInterval
                            .overlap(midnightInterval));
                }
            }
            this.midnightWorkingMinute -= (this.midnightWorkingMinute % actualWorkPlace
                    .getUnitTime().getMinuteOfHour());

        } else if (beginTime == null && finishTime == null) {
            if (isWorkDay()) {
                this.absence = true;
            }
        }

        this.tardyOrEarlyLeaving = actualWorkPlace.isTardyOrEarlyLeaving(beginTime, finishTime);

        if (tardyOrEarlyLeaving) {
            this.compensationMinute = 0;
        } else {
            this.specialWorkCode = null;
        }

    }

    public void setDefault(WorkPlace defaultWorkPlace) {
        setBeginTime(defaultWorkPlace.getBaseBeginTime());
        setFinishTime(defaultWorkPlace.getBaseFinishTime());
    }

    public boolean isWorkDay() {
        if (targetDate == null) {
            return false;
        }
        return !((targetDate.getDayOfWeek() == DateTimeConstants.SATURDAY) || (targetDate
                .getDayOfWeek() == DateTimeConstants.SUNDAY));
    }

    private void clearCalculate() {
        this.actualWorkingMinute = 0;
        this.compensationMinute = 0;
        this.midnightWorkingMinute = 0;
        this.tardyOrEarlyLeaving = false;
        this.absence = false;
    }

    private int calculateIntervalMinute(Interval interval) {
        return Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(interval.toDuration().getMillis()))
                .intValue();
    }

}
