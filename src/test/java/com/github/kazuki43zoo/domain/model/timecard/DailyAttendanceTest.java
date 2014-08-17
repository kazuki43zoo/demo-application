package com.github.kazuki43zoo.domain.model.timecard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

public class DailyAttendanceTest {
    private WorkPlace mainOffice;
    private WorkPlace officeA;

    public DailyAttendanceTest() {
        this.mainOffice = new WorkPlace();
        mainOffice.setBaseBeginTime(LocalTime.parse("09:00"));
        mainOffice.setBaseFinishTime(LocalTime.parse("17:45"));
        mainOffice.setUnitTime(LocalTime.parse("00:30"));
        mainOffice.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("00:00"),
                LocalTime.parse("01:00")), new BreakTime("uuid", LocalTime.parse("07:30"),
                LocalTime.parse("09:00")), new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00")), new BreakTime("uuid", LocalTime.parse("17:45"),
                LocalTime.parse("18:15")), new BreakTime("uuid", LocalTime.parse("21:45"),
                LocalTime.parse("22:00"))));
        mainOffice.initialize();

        this.officeA = new WorkPlace();
        officeA.setBaseBeginTime(LocalTime.parse("10:00"));
        officeA.setBaseFinishTime(LocalTime.parse("18:30"));
        officeA.setUnitTime(LocalTime.parse("00:30"));
        officeA.setBreakTimes(Arrays.asList(new BreakTime("uuid", LocalTime.parse("00:00"),
                LocalTime.parse("01:00")), new BreakTime("uuid", LocalTime.parse("07:30"),
                LocalTime.parse("09:00")), new BreakTime("uuid", LocalTime.parse("12:00"),
                LocalTime.parse("13:00")), new BreakTime("uuid", LocalTime.parse("18:45"),
                LocalTime.parse("19:15")), new BreakTime("uuid", LocalTime.parse("21:45"),
                LocalTime.parse("22:00"))));
        officeA.initialize();
    }

    @Test
    public void calculateBaseWorkingOnMainOffice() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(LocalTime.parse("09:00"));
        attendance.setFinishTime(LocalTime.parse("17:45"));

        attendance.calculate(mainOffice, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(465));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateMidnightWorking() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(LocalTime.parse("09:00"));
        attendance.setFinishTime(LocalTime.parse("09:00"));

        attendance.calculate(mainOffice, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(1185));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(360));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateCompensationWorking() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(LocalTime.parse("10:00"));
        attendance.setFinishTime(LocalTime.parse("18:30"));

        attendance.calculate(officeA, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(450));
        assertThat(attendance.getCompensationMinute(), is(15));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateNotCompensationWorkingBecauseTardy() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(LocalTime.parse("10:01"));
        attendance.setFinishTime(LocalTime.parse("18:30"));

        attendance.calculate(officeA, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(420));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(true));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateCompensationWorkingNotWorkDay() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setTargetDate(LocalDate.parse("2014-08-17"));
        attendance.setBeginTime(LocalTime.parse("10:00"));
        attendance.setFinishTime(LocalTime.parse("18:30"));

        attendance.calculate(officeA, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(450));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateNotWorking() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(null);
        attendance.setFinishTime(null);

        attendance.calculate(officeA, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(0));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(true));
    }

    @Test
    public void calculateCompensatoryHoliday() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(null);
        attendance.setFinishTime(null);
        attendance.setSpecialWorkCode("10");

        attendance.calculate(officeA, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(0));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateBeginOnly() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(LocalTime.parse("10:00"));
        attendance.setFinishTime(null);

        attendance.calculate(officeA, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(0));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateFinishOnly() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(null);
        attendance.setFinishTime(LocalTime.parse("18:29"));

        attendance.calculate(officeA, mainOffice);

        assertThat(attendance.getFinishTime(), is(nullValue()));
        assertThat(attendance.getActualWorkingMinute(), is(0));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(true));
    }

    @Test
    public void calculatePaidLeave() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(null);
        attendance.setFinishTime(null);
        attendance.setPaidLeave(true);

        attendance.calculate(officeA, mainOffice);

        assertThat(attendance.getBeginTime(), is(LocalTime.parse("09:00")));
        assertThat(attendance.getFinishTime(), is(LocalTime.parse("17:45")));
        assertThat(attendance.getActualWorkingMinute(), is(465));
        assertThat(attendance.getCompensationMinute(), is(0));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateOverrideWorkPlace() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(LocalTime.parse("10:00"));
        attendance.setFinishTime(LocalTime.parse("18:30"));
        attendance.setWorkPlace(officeA);

        attendance.calculate(mainOffice, mainOffice);

        assertThat(attendance.getActualWorkingMinute(), is(450));
        assertThat(attendance.getCompensationMinute(), is(15));
        assertThat(attendance.getMidnightWorkingMinute(), is(0));
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));
        assertThat(attendance.isAbsence(), is(false));
    }

    @Test
    public void calculateWorkDay() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(LocalTime.parse("10:00"));
        attendance.setFinishTime(LocalTime.parse("18:29"));

        attendance.setTargetDate(LocalDate.parse("2014-08-18"));
        attendance.calculate(mainOffice, mainOffice);
        assertThat(attendance.isTardyOrEarlyLeaving(), is(true));

        attendance.setTargetDate(LocalDate.parse("2014-08-19"));
        attendance.calculate(mainOffice, mainOffice);
        assertThat(attendance.isTardyOrEarlyLeaving(), is(true));

        attendance.setTargetDate(LocalDate.parse("2014-08-20"));
        attendance.calculate(mainOffice, mainOffice);
        assertThat(attendance.isTardyOrEarlyLeaving(), is(true));

        attendance.setTargetDate(LocalDate.parse("2014-08-21"));
        attendance.calculate(mainOffice, mainOffice);
        assertThat(attendance.isTardyOrEarlyLeaving(), is(true));

        attendance.setTargetDate(LocalDate.parse("2014-08-22"));
        attendance.calculate(mainOffice, mainOffice);
        assertThat(attendance.isTardyOrEarlyLeaving(), is(true));
    }

    @Test
    public void calculateNotWorkDay() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setBeginTime(LocalTime.parse("10:00"));
        attendance.setFinishTime(LocalTime.parse("18:29"));

        attendance.setTargetDate(LocalDate.parse("2014-08-23"));
        attendance.calculate(mainOffice, mainOffice);
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));

        attendance.setTargetDate(LocalDate.parse("2014-08-24"));
        attendance.calculate(mainOffice, mainOffice);
        assertThat(attendance.isTardyOrEarlyLeaving(), is(false));

    }

    @Test
    public void setDefault() {
        DailyAttendance attendance = new DailyAttendance();
        attendance.setDefault(mainOffice);
        assertThat(attendance.getBeginTime(), is(LocalTime.parse("09:00")));
        assertThat(attendance.getFinishTime(), is(LocalTime.parse("17:45")));

        attendance.setDefault(officeA);
        assertThat(attendance.getBeginTime(), is(LocalTime.parse("10:00")));
        assertThat(attendance.getFinishTime(), is(LocalTime.parse("18:30")));
    }

}
