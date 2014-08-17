package com.github.kazuki43zoo.domain.model.timecard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class BreakTime implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final LocalDate BASE_DATE = new LocalDate(0);

    private final String workPlaceUuid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime finishTime;

    private String note;

    private String noteJa;

    private final Interval breakTimeBaseInterval;

    @Getter(AccessLevel.NONE)
    private final List<Interval> breakTimeIntervals = new ArrayList<>();

    public BreakTime(final String workPlaceUuid, final LocalTime beginTime,
            final LocalTime finishTime) {
        this.workPlaceUuid = workPlaceUuid;
        this.beginTime = beginTime;
        this.finishTime = finishTime;
        this.breakTimeBaseInterval = new Interval(BASE_DATE.toDateTime(beginTime),
                BASE_DATE.toDateTime(finishTime));
        breakTimeIntervals.add(breakTimeBaseInterval);
        breakTimeIntervals.add(new Interval(BASE_DATE.toDateTime(beginTime).plusDays(1), BASE_DATE
                .toDateTime(finishTime).plusDays(1)));
    }

    int calculateContainsMinute(final Interval workTimeInterval) {
        long minute = 0;
        for (final Interval breakTimeInterval : breakTimeIntervals) {
            if (workTimeInterval.overlaps(breakTimeInterval)) {
                minute += toMinute(workTimeInterval.overlap(breakTimeInterval));
            }
        }
        return (int) minute;
    }

    boolean isFuture(final Interval workTimeInterval) {
        return breakTimeBaseInterval.isAfter(workTimeInterval);
    }

    private long toMinute(final Interval interval) {
        return TimeUnit.MILLISECONDS.toMinutes(interval.toDuration().getMillis());
    }

}
