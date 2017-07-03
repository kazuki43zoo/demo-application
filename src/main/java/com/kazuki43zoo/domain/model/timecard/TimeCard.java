package com.kazuki43zoo.domain.model.timecard;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Data
public class TimeCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accountUuid;

    private LocalDate targetMonth;

    private WorkPlace workPlace;

    private String note;

    private List<DailyAttendance> attendances;

    public TimeCard addAttendance(final DailyAttendance attendance) {
        if (attendances == null) {
            attendances = new ArrayList<>();
        }
        attendances.add(attendance);
        return this;
    }

}
