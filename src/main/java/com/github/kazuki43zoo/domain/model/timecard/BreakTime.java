package com.github.kazuki43zoo.domain.model.timecard;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
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

    public BreakTime(final String workPlaceUuid, final LocalTime beginTime, final LocalTime finishTime) {
        this.workPlaceUuid = workPlaceUuid;
        this.beginTime = beginTime;
        this.finishTime = finishTime;
    }

    List<Interval> toBreakTimeIntervals() {
        final List<Interval> breakTimeIntervals = new ArrayList<>();
        final Interval breakTimeInterval = new Interval(BASE_DATE.toDateTime(beginTime),
                BASE_DATE.toDateTime(finishTime));
        breakTimeIntervals.add(breakTimeInterval);
        breakTimeIntervals.add(new Interval(breakTimeInterval.getStart().plusDays(1),
                breakTimeInterval.getEnd().plusDays(1)));
        return breakTimeIntervals;
    }

}
