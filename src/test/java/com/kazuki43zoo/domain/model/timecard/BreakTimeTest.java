package com.kazuki43zoo.domain.model.timecard;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

public class BreakTimeTest {
    private static final LocalDate BASE_DATE = new LocalDate(0);

    @Test
    public void toBreakTimeIntervalsPattern1() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("00:00"),
                LocalTime.parse("01:00"));

        List<Interval> breakTimeIntervals = breakTime.toBreakTimeIntervals();

        Interval breakTimeInterval = breakTimeIntervals.get(0);
        assertThat(breakTimeInterval.getStart(), is(BASE_DATE.toDateTime(LocalTime.parse("00:00"))));
        assertThat(breakTimeInterval.getEnd(), is(BASE_DATE.toDateTime(LocalTime.parse("01:00"))));

        breakTimeInterval = breakTimeIntervals.get(1);
        assertThat(breakTimeInterval.getStart(), is(BASE_DATE.toDateTime(LocalTime.parse("00:00"))
                .plusDays(1)));
        assertThat(breakTimeInterval.getEnd(), is(BASE_DATE.toDateTime(LocalTime.parse("01:00"))
                .plusDays(1)));

    }

    @Test
    public void toBreakTimeIntervalsPattern2() {
        BreakTime breakTime = new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"));

        List<Interval> breakTimeIntervals = breakTime.toBreakTimeIntervals();

        Interval breakTimeInterval = breakTimeIntervals.get(0);
        assertThat(breakTimeInterval.getStart(), is(BASE_DATE.toDateTime(LocalTime.parse("12:00"))));
        assertThat(breakTimeInterval.getEnd(), is(BASE_DATE.toDateTime(LocalTime.parse("13:00"))));

        breakTimeInterval = breakTimeIntervals.get(1);
        assertThat(breakTimeInterval.getStart(), is(BASE_DATE.toDateTime(LocalTime.parse("12:00"))
                .plusDays(1)));
        assertThat(breakTimeInterval.getEnd(), is(BASE_DATE.toDateTime(LocalTime.parse("13:00"))
                .plusDays(1)));

    }

}
