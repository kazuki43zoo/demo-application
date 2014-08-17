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
    private static final LocalDate BASE_DATE = new LocalDate(0);
    private static final long serialVersionUID = 1L;

    private final String workPlaceUuid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime finishTime;

    private String note;

    private String noteJa;

    @Getter(AccessLevel.NONE)
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
        long minute = 0;
        for (Interval midnightInterval : breakTimeIntervals) {
            if (workTimeInterval.overlaps(midnightInterval)) {
                minute += toMinute(workTimeInterval.overlap(midnightInterval));
            }
        }
        return (int) minute;
    }

    private long toMinute(Interval interval) {
        return TimeUnit.MILLISECONDS.toMinutes(interval.toDuration().getMillis());
    }

}
