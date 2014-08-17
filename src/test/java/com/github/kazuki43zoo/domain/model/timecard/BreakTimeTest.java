package com.github.kazuki43zoo.domain.model.timecard;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

public class BreakTimeTest {
    private static final LocalDate BASE_DATE = new LocalDate(0);

    @Test
    public void calculateContainsWorkTimeIntervalIsJustContain() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("00:00"),
                LocalTime.parse("01:00"));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("00:00")),
                BASE_DATE.toDateTime(LocalTime.parse("01:00")));

        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(60));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsForwardMatch() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("01:00"),
                LocalTime.parse("02:00"));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("00:59")),
                BASE_DATE.toDateTime(LocalTime.parse("01:15")));

        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(15));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsBackwardMatch() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("01:00"),
                LocalTime.parse("02:00"));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("01:45")),
                BASE_DATE.toDateTime(LocalTime.parse("02:01")));

        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(15));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsContain() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("00:00"),
                LocalTime.parse("01:00"));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("00:01")),
                BASE_DATE.toDateTime(LocalTime.parse("00:59")));

        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(58));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsInclude() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("01:00"),
                LocalTime.parse("02:00"));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("00:59")),
                BASE_DATE.toDateTime(LocalTime.parse("02:01")));

        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(60));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsBeforeRange() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("01:00"),
                LocalTime.parse("02:00"));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("00:00")),
                BASE_DATE.toDateTime(LocalTime.parse("01:00")));

        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(0));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsAfterRange() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("01:00"),
                LocalTime.parse("02:00"));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("02:00")),
                BASE_DATE.toDateTime(LocalTime.parse("03:00")));

        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(0));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsMidnight() {
        // 00:00 - 01:00 and 24:00 - 25:00
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("00:00"),
                LocalTime.parse("01:00"));

        // 24:00 - 25:00
        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("00:00"))
                .plusDays(1), BASE_DATE.toDateTime(LocalTime.parse("01:00")).plusDays(1));

        // 60 (00:00-01:00)
        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(60));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsMultipleMatch() {
        // 00:00 - 01:00 and 24:00 - 25:00
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("00:00"),
                LocalTime.parse("01:00"));

        // 00:30 - 24:30
        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("00:30")),
                BASE_DATE.toDateTime(LocalTime.parse("00:30")).plusDays(1));

        // 30 (00:30-01:00) + 30(24:00-24:30)
        assertThat(breakTime.calculateContainsMinute(workTimeInterval), is(60));
    }

}
