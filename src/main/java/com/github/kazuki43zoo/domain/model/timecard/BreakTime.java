package com.github.kazuki43zoo.domain.model.timecard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Data;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@Data
public class BreakTime implements Serializable {
    private static final LocalDate BASE_DATE = new LocalDate(0);
    private static final long serialVersionUID = 1L;

    private final String workPlaceUuid;
    private final LocalTime beginTime;
    private final LocalTime finishTime;

    private final List<Interval> breakTimeIntervals = new ArrayList<>();

    public BreakTime(String workPlaceUuid, LocalTime beginTime, LocalTime finishTime) {
        this.workPlaceUuid = workPlaceUuid;
        this.beginTime = beginTime;
        this.finishTime = finishTime;
        breakTimeIntervals.add(new Interval(BASE_DATE.toDateTime(beginTime), BASE_DATE
                .toDateTime(finishTime)));
        breakTimeIntervals.add(new Interval(BASE_DATE.toDateTime(beginTime).plusDays(1), BASE_DATE
                .toDateTime(finishTime).plusDays(1)));
    }

    public int calculateContainsMinute(Interval workTimeInterval) {
        int minute = 0;
        for (Interval midnightInterval : breakTimeIntervals) {
            if (workTimeInterval.overlaps(midnightInterval)) {
                minute += toMinute(workTimeInterval.overlap(midnightInterval));
            }
        }
        return minute;
    }

    private int toMinute(Interval interval) {
        return Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(interval.toDuration().getMillis()))
                .intValue();
    }

}
