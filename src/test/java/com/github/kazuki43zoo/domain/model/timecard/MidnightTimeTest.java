package com.github.kazuki43zoo.domain.model.timecard;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

public class MidnightTimeTest {
    private static final LocalDate BASE_DATE = new LocalDate(0);
    private MidnightTime midnightTime = MidnightTime.INSTANCE;
    private WorkPlace workPlace;

    public MidnightTimeTest() {
        this.workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("00:30")); // 30 minute
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("00:00"),
                LocalTime.parse("01:00"))));
        workPlace.initialize();
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsJustContain() {

        // 22:00 - 05:00(29:00)
        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("22:00")),
                BASE_DATE.toDateTime(LocalTime.parse("05:00")).plusDays(1));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(360));

    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsForwardMatch() {

        // 21:00 - 22:30
        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("21:00")),
                BASE_DATE.toDateTime(LocalTime.parse("22:30")));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(30));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsBackwardMatch() {
        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("04:00")),
                BASE_DATE.toDateTime(LocalTime.parse("06:00")));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(60));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsContain() {

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("23:00")),
                BASE_DATE.toDateTime(LocalTime.parse("04:00")).plusDays(1));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(240));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsInclude() {

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("21:00")),
                BASE_DATE.toDateTime(LocalTime.parse("06:00")).plusDays(1));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(360));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsBeforeRange() {

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("05:00")),
                BASE_DATE.toDateTime(LocalTime.parse("22:00")));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(0));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsAfterRange() {

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("05:00"))
                .plusDays(1), BASE_DATE.toDateTime(LocalTime.parse("22:00")).plusDays(1));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(0));
    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsPattern1Of24Hour() {

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("00:00")),
                BASE_DATE.toDateTime(LocalTime.parse("00:00")).plusDays(1));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(360));

    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsPattern2Of24Hour() {

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("22:00")),
                BASE_DATE.toDateTime(LocalTime.parse("22:00")).plusDays(1));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(360));

    }

    @Test
    public void calculateContainsMinuteWorkTimeIntervalIsPattern3Of24Hour() {

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("23:59")),
                BASE_DATE.toDateTime(LocalTime.parse("23:59")).plusDays(1));

        assertThat(midnightTime.calculateContainsMinute(workTimeInterval, workPlace), is(360));

    }

}
