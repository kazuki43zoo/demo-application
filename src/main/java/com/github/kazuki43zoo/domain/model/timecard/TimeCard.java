package com.github.kazuki43zoo.domain.model.timecard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeCard implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private LocalDate targetMonth;
    private WorkPlace workPlace;
    private List<DailyAttendance> attendances;

    public TimeCard addAttendance(DailyAttendance attendance) {
        if (attendances == null) {
            attendances = new ArrayList<>();
        }
        attendances.add(attendance);
        return this;
    }

}
