package com.github.kazuki43zoo.domain.model.timecard;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

public class WorkPlaceTest {

    private static final LocalDate BASE_DATE = new LocalDate(0);

    @Test
    public void initialize() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        assertThat(workPlace.getBaseWorkTimeMinute(), is(465));
    }

    @Test
    public void initializeBreakTimesNotSpecify() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));

        workPlace.initialize();

        assertThat(workPlace.getBaseWorkTimeMinute(), is(525));
    }

    @Test
    public void calculateWorkingMinuteWorkTimeIntervalIsJustBaseWorkTime() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:30"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("9:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:45")));

        assertThat(workPlace.calculateWorkingMinute(workTimeInterval), is(465));
    }

    @Test
    public void calculateWorkingMinuteWorkTimeIntervalIsUnderBaseWorkTime() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:30"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("9:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:44")));

        assertThat(workPlace.calculateWorkingMinute(workTimeInterval), is(450));
    }

    @Test
    public void calculateWorkingMinuteWorkTimeIntervalIsOverBaseWorkTime() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:30"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("9:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:46")));

        assertThat(workPlace.calculateWorkingMinute(workTimeInterval), is(465));
    }

    @Test
    public void truncateWithTimeUnitWorkTimeMinuteEqualsBaseWorkTimeMinute() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:30"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        assertThat(workPlace.truncateWithTimeUnit(465), is(465));
    }

    @Test
    public void truncateWithTimeUnitWorkTimeMinuteIsUnderBaseWorkTimeMinute() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:30"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        assertThat(workPlace.truncateWithTimeUnit(464), is(450));
    }

    @Test
    public void truncateWithTimeUnitWorkTimeMinuteIsOverBaseWorkTimeMinute() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:30"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        assertThat(workPlace.truncateWithTimeUnit(466), is(465));
    }

    @Test
    public void truncateWithTimeUnitPattern1() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:01"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        assertThat(workPlace.truncateWithTimeUnit(464), is(464));
        assertThat(workPlace.truncateWithTimeUnit(466), is(466));
    }

    @Test
    public void truncateWithTimeUnitPattern2() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:05"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        assertThat(workPlace.truncateWithTimeUnit(464), is(460));
        assertThat(workPlace.truncateWithTimeUnit(469), is(465));
        assertThat(workPlace.truncateWithTimeUnit(470), is(470));
    }

    @Test
    public void truncateWithTimeUnitPattern3() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("0:15"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        assertThat(workPlace.truncateWithTimeUnit(450), is(450));
        assertThat(workPlace.truncateWithTimeUnit(449), is(435));
        assertThat(workPlace.truncateWithTimeUnit(479), is(465));
        assertThat(workPlace.truncateWithTimeUnit(480), is(480));
    }

    @Test
    public void truncateWithTimeUnitPattern4() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.setUnitTime(LocalTime.parse("1:00"));
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        workPlace.initialize();

        assertThat(workPlace.truncateWithTimeUnit(465), is(465));
        assertThat(workPlace.truncateWithTimeUnit(464), is(420));
        assertThat(workPlace.truncateWithTimeUnit(524), is(465));
        assertThat(workPlace.truncateWithTimeUnit(525), is(525));
    }

    @Test
    public void isTardyOrEarlyLeavingWorkingIsNotTardyOrEarlyLeavingPattern1() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.initialize();

        assertThat(workPlace.isTardyOrEarlyLeaving(BASE_DATE.toDateTime(LocalTime.parse("9:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:45"))), is(false));
    }

    @Test
    public void isTardyOrEarlyLeavingWorkingIsNotTardyOrEarlyLeavingPattern2() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.initialize();

        assertThat(workPlace.isTardyOrEarlyLeaving(BASE_DATE.toDateTime(LocalTime.parse("8:59")),
                BASE_DATE.toDateTime(LocalTime.parse("17:46"))), is(false));
    }

    @Test
    public void isTardyOrEarlyLeavingWorkingIsTardy() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.initialize();

        assertThat(workPlace.isTardyOrEarlyLeaving(BASE_DATE.toDateTime(LocalTime.parse("9:01")),
                BASE_DATE.toDateTime(LocalTime.parse("17:45"))), is(true));
    }

    @Test
    public void isTardyOrEarlyLeavingWorkingIsEarlyLeaving() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.initialize();

        assertThat(workPlace.isTardyOrEarlyLeaving(BASE_DATE.toDateTime(LocalTime.parse("9:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:44"))), is(true));
    }

    @Test
    public void isTardyOrEarlyLeavingWorkingIsTardyAndEarlyLeaving() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.initialize();

        assertThat(workPlace.isTardyOrEarlyLeaving(BASE_DATE.toDateTime(LocalTime.parse("9:01")),
                BASE_DATE.toDateTime(LocalTime.parse("17:44"))), is(true));
    }

    @Test
    public void isTardyOrEarlyLeavingBeginTimeIsNull() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.initialize();

        assertThat(
                workPlace.isTardyOrEarlyLeaving(null,
                        BASE_DATE.toDateTime(LocalTime.parse("17:45"))), is(false));
        assertThat(
                workPlace.isTardyOrEarlyLeaving(null,
                        BASE_DATE.toDateTime(LocalTime.parse("17:44"))), is(true));
    }

    @Test
    public void isTardyOrEarlyLeavingFinishTimeIsNull() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));
        workPlace.initialize();

        assertThat(workPlace.isTardyOrEarlyLeaving(BASE_DATE.toDateTime(LocalTime.parse("09:00")),
                null), is(false));
        assertThat(workPlace.isTardyOrEarlyLeaving(BASE_DATE.toDateTime(LocalTime.parse("09:01")),
                null), is(true));
    }

    @Test
    public void isTardyOrEarlyLeavingBeginAndFinishTimeIsNull() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBaseBeginTime(LocalTime.parse("09:00"));
        workPlace.setBaseFinishTime(LocalTime.parse("17:45"));

        assertThat(workPlace.isTardyOrEarlyLeaving(null, null), is(false));
    }

    @Test
    public void calculateContainsBreakTimeMinuteBreakTimesNotSpecify() {
        WorkPlace workPlace = new WorkPlace();

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("9:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:45")));

        assertThat(workPlace.calculateContainsBreakTimeMinute(workTimeInterval), is(0));

    }

    @Test
    public void calculateContainsBreakTimeMinuteSpecifyBreakTimes() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00"))));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("09:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:45")));

        assertThat(workPlace.calculateContainsBreakTimeMinute(workTimeInterval), is(60));
    }

    @Test
    public void calculateContainsBreakTimeMinuteSpecifyMultipleBreakTimesAllMatch() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00")), new BreakTime("uuid", LocalTime.parse("17:45"),
                LocalTime.parse("18:15"))));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("12:59")),
                BASE_DATE.toDateTime(LocalTime.parse("17:46")));

        assertThat(workPlace.calculateContainsBreakTimeMinute(workTimeInterval), is(2));
    }

    @Test
    public void calculateContainsBreakTimeMinuteSpecifyMultipleBreakTimesFirstMatch() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00")), new BreakTime("uuid", LocalTime.parse("17:45"),
                LocalTime.parse("18:15"))));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("09:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:45")));

        assertThat(workPlace.calculateContainsBreakTimeMinute(workTimeInterval), is(60));
    }

    @Test
    public void calculateContainsBreakTimeMinuteSpecifyMultipleBreakTimesLastMatch() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00")), new BreakTime("uuid", LocalTime.parse("17:45"),
                LocalTime.parse("18:15"))));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("13:00")),
                BASE_DATE.toDateTime(LocalTime.parse("18:16")));

        assertThat(workPlace.calculateContainsBreakTimeMinute(workTimeInterval), is(30));
    }

    @Test
    public void calculateContainsBreakTimeMinuteSpecifyMultipleBreakTimesNotMatchPattern1() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00")), new BreakTime("uuid", LocalTime.parse("17:45"),
                LocalTime.parse("18:15"))));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("09:00")),
                BASE_DATE.toDateTime(LocalTime.parse("12:00")));

        assertThat(workPlace.calculateContainsBreakTimeMinute(workTimeInterval), is(0));
    }

    @Test
    public void calculateContainsBreakTimeMinuteSpecifyMultipleBreakTimesNotMatchPattern2() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00")), new BreakTime("uuid", LocalTime.parse("17:45"),
                LocalTime.parse("18:15"))));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("13:00")),
                BASE_DATE.toDateTime(LocalTime.parse("17:45")));

        assertThat(workPlace.calculateContainsBreakTimeMinute(workTimeInterval), is(0));
    }

    @Test
    public void calculateContainsBreakTimeMinuteSpecifyMultipleBreakTimesNotMatchPattern3() {
        WorkPlace workPlace = new WorkPlace();
        workPlace.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00")), new BreakTime("uuid", LocalTime.parse("17:45"),
                LocalTime.parse("18:15"))));

        Interval workTimeInterval = new Interval(BASE_DATE.toDateTime(LocalTime.parse("18:15")),
                BASE_DATE.toDateTime(LocalTime.parse("22:00")));

        assertThat(workPlace.calculateContainsBreakTimeMinute(workTimeInterval), is(0));
    }

}
