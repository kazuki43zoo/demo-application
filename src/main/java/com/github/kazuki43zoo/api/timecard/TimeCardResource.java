package com.github.kazuki43zoo.api.timecard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.kazuki43zoo.infra.jackson.map.EmptyStringSerialilzer;

import lombok.Data;

@Data
public class TimeCardResource implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(nullsUsing = EmptyStringSerialilzer.class)
    private String workPlaceUuid;

    @Valid
    private List<DailyAttendanceResource> attendances;

    private boolean stored;

    public TimeCardResource addDailyAttendance(DailyAttendanceResource dailyAttendanceResource) {
        if (attendances == null) {
            this.attendances = new ArrayList<>();
        }
        attendances.add(dailyAttendanceResource);
        return this;
    }
}
