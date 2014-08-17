package com.github.kazuki43zoo.domain.model.timecard;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @Setter(AccessLevel.NONE)
    transient private int actualWorkingMinute;

    @Setter(AccessLevel.NONE)
    transient private int compensationMinute;

    @Setter(AccessLevel.NONE)
    transient private int midnightWorkingMinute;

    @Setter(AccessLevel.NONE)
    transient private boolean tardyOrEarlyLeaving;

    @Setter(AccessLevel.NONE)
    transient private boolean absence;

    public void calculate(final WorkPlace defaultWorkPlace, final WorkPlace mainOffice) {

        clearCalculate();

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
            this.actualWorkingMinute = actualWorkPlace.calculateWorkingMinute(workTimeInterval);

            // calculate compensation minute
            if (actualWorkingMinute < mainOffice.getBaseWorkTimeMinute() && isWorkDay()) {
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

        if (isWorkDay()) {
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

    private boolean isWorkDay() {
        if (targetDate == null) {
            return true;
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

}
