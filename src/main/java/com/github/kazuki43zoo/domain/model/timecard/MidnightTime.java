package com.github.kazuki43zoo.domain.model.timecard;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MidnightTime {

    private static final LocalDate BASE_DATE = new LocalDate(0);

    private static final LocalTime MIDNIGHT_BEGIN_TIME = LocalTime.parse("22:00");

    private static final LocalTime MIDNIGHT_FINISH_TIME = LocalTime.parse("05:00");

    private final List<Interval> midnightIntervals = new ArrayList<>();

    static final MidnightTime INSTANCE = new MidnightTime();

    private MidnightTime() {
        // 00:00-05:00
        midnightIntervals.add(new Interval(
                BASE_DATE.toDateTimeAtStartOfDay(),
                BASE_DATE.toDateTime(MIDNIGHT_FINISH_TIME)));
        // 22:00-05:00(29:00)
        midnightIntervals.add(new Interval(
                BASE_DATE.toDateTime(MIDNIGHT_BEGIN_TIME),
                BASE_DATE.toDateTime(MIDNIGHT_FINISH_TIME).plusDays(1)));
        // 22:00(46:00)-00:00(48:00)
        midnightIntervals.add(new Interval(
                BASE_DATE.toDateTime(MIDNIGHT_BEGIN_TIME).plusDays(1),
                BASE_DATE.toDateTime(MIDNIGHT_BEGIN_TIME).plusDays(1).plusHours(2)));
    }

    int calculateContainsMinute(final Interval workTimeInterval, final WorkPlace workPlace) {
        long minute = 0;
        for (final Interval midnightInterval : midnightIntervals) {
            if (workTimeInterval.overlaps(midnightInterval)) {
                final Interval overlappedInterval = workTimeInterval.overlap(midnightInterval);
                minute += (toMinute(overlappedInterval) - workPlace.calculateContainsBreakTimeMinute(overlappedInterval));
            } else if (midnightInterval.isAfter(workTimeInterval)) {
                break;
            }
        }
        return minute == 0 ? 0 : workPlace.truncateWithTimeUnit((int) minute);
    }

    private long toMinute(final Interval interval) {
        return TimeUnit.MILLISECONDS.toMinutes(interval.toDuration().getMillis());
    }

}
