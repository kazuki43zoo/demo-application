package com.github.kazuki43zoo.domain.model.timecard;

import com.github.kazuki43zoo.domain.model.calendar.Holiday;
import org.joda.time.*;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data
public class DailyAttendance implements Serializable {

    private static final LocalDate BASE_DATE = new LocalDate(0);

    private static final long serialVersionUID = 1L;

    private String accountUuid;

    private LocalDate targetDate;

    private LocalTime beginTime;

    private LocalTime finishTime;

    private boolean paidLeave;

    private String specialWorkCode;

    private String note;

    private WorkPlace workPlace;

    @lombok.Setter(lombok.AccessLevel.NONE)
    transient private int actualWorkingMinute;

    @lombok.Setter(lombok.AccessLevel.NONE)
    transient private int compensationMinute;

    @lombok.Setter(lombok.AccessLevel.NONE)
    transient private int midnightWorkingMinute;

    @lombok.Setter(lombok.AccessLevel.NONE)
    transient private boolean tardyOrEarlyLeaving;

    @lombok.Setter(lombok.AccessLevel.NONE)
    transient private boolean absence;

    @lombok.Setter(lombok.AccessLevel.NONE)
    transient private boolean holiday;

    public void calculate(final WorkPlace defaultWorkPlace, final WorkPlace mainOffice) {
        Map<LocalDate, Holiday> holidays = Collections.emptyMap();
        calculate(defaultWorkPlace, mainOffice, holidays);
    }

    public void calculate(final WorkPlace defaultWorkPlace, final WorkPlace mainOffice,
                          final Map<LocalDate, Holiday> holidays) {

        clearCalculate();

        if (targetDate != null) {
            if ((targetDate.getDayOfWeek() == DateTimeConstants.SATURDAY)
                    || (targetDate.getDayOfWeek() == DateTimeConstants.SUNDAY)
                    || holidays.containsKey(targetDate)
                    || (holidays.containsKey(targetDate.minusDays(1)) && holidays.containsKey(targetDate.plusDays(1)))) {
                this.holiday = true;
            }
        }

        if (paidLeave) {
            setBeginTime(mainOffice.getBaseBeginTime());
            setFinishTime(mainOffice.getBaseFinishTime());
            this.actualWorkingMinute = mainOffice.getBaseWorkTimeMinute();
            return;
        }

        WorkPlace actualWorkPlace = workPlace;
        if (actualWorkPlace == null) {
            actualWorkPlace = defaultWorkPlace;
        }

        DateTime beginDateTime = null;
        if (beginTime != null) {
            beginDateTime = BASE_DATE.toDateTime(beginTime);
        }
        DateTime finishDateTime = null;
        if (finishTime != null) {
            finishDateTime = BASE_DATE.toDateTime(finishTime);
        }

        if (beginDateTime != null && finishDateTime != null) {

            // decide work time interval
            if (!beginDateTime.isBefore(finishDateTime)) {
                finishDateTime = finishDateTime.plusDays(1);
            }
            final Interval workTimeInterval = new Interval(beginDateTime, finishDateTime);

            // calculate working minute
            this.actualWorkingMinute = actualWorkPlace.calculateWorkingMinute(workTimeInterval,
                    mainOffice);

            // calculate compensation minute
            if (actualWorkingMinute < mainOffice.getBaseWorkTimeMinute() && !holiday) {
                this.compensationMinute = mainOffice.getBaseWorkTimeMinute() - actualWorkingMinute;
            }

            // calculate midnight working minute
            this.midnightWorkingMinute = MidnightTime.INSTANCE.calculateContainsMinute(
                    workTimeInterval, actualWorkPlace);

        } else {
            if (finishDateTime != null) {
                finishDateTime = null;
                setFinishTime(null);
            }
        }

        if (!holiday) {
            this.tardyOrEarlyLeaving = actualWorkPlace.isTardyOrEarlyLeaving(beginDateTime,
                    finishDateTime);
            if (tardyOrEarlyLeaving) {
                this.compensationMinute = 0;
            } else if (beginDateTime == null && !StringUtils.hasLength(specialWorkCode)) {
                this.absence = true;
            }
        }

    }

    public void setDefault(final WorkPlace defaultWorkPlace) {
        setBeginTime(defaultWorkPlace.getBaseBeginTime());
        setFinishTime(defaultWorkPlace.getBaseFinishTime());
    }

    private void clearCalculate() {
        this.actualWorkingMinute = 0;
        this.compensationMinute = 0;
        this.midnightWorkingMinute = 0;
        this.tardyOrEarlyLeaving = false;
        this.absence = false;
    }

}
