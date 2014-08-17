package com.github.kazuki43zoo.domain.model.timecard;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkPlace implements Serializable {

    public static final String MAIN_OFFICE_UUID = "00000000-0000-0000-0000-000000000000";
    private static final LocalDate BASE_DATE = new LocalDate(0);
    private static final long serialVersionUID = 1L;

    private String workPlaceUuid;
    private String workPlaceName;
    private String workPlaceNameJa;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime baseBeginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime baseFinishTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "H:mm")
    private LocalTime unitTime;
    private List<BreakTime> breakTimes;

    @Setter(AccessLevel.NONE)
    private transient int baseWorkTimeMinute;

    public void initialize() {

        Interval baseWorkTimeInterval = new Interval(BASE_DATE.toDateTime(baseBeginTime),
                BASE_DATE.toDateTime(baseFinishTime));
        this.baseWorkTimeMinute = (int) toMinute(baseWorkTimeInterval)
                - calculateContainsBreakTimeMinute(baseWorkTimeInterval);
    }

    public int calculateWorkingMinute(Interval workTimeInterval) {
        int workingMinute = (int) toMinute(workTimeInterval)
                - calculateContainsBreakTimeMinute(workTimeInterval);
        return truncateWithTimeUnit(workingMinute);
    }

    public int truncateWithTimeUnit(int minute) {
        int determinedMinute = 0;
        int undeterminedMinute = minute;
        if (baseWorkTimeMinute <= minute) {
            determinedMinute = baseWorkTimeMinute;
            undeterminedMinute = minute - baseWorkTimeMinute;
        }
        return determinedMinute + undeterminedMinute
                - (undeterminedMinute % getUnitTime().getMinuteOfHour());
    }

    public boolean isTardyOrEarlyLeaving(LocalTime beginTime, LocalTime finishTime) {
        if (beginTime != null && beginTime.isAfter(baseBeginTime)) {
            return true;
        }
        if (finishTime != null && finishTime.isBefore(baseFinishTime)) {
            return true;
        }
        return false;
    }

    private int calculateContainsBreakTimeMinute(Interval workTimeInterval) {
        int minute = 0;
        for (BreakTime breakTime : getBreakTimes()) {
            minute += breakTime.calculateContainsMinute(workTimeInterval);
        }
        return minute;
    }

    private long toMinute(Interval interval) {
        return TimeUnit.MILLISECONDS.toMinutes(interval.toDuration().getMillis());
    }

}
