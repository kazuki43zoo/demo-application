package com.github.kazuki43zoo.api.timecard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Null;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.github.kazuki43zoo.infra.jackson.map.EmptyStringSerialilzer;

@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.ToString(callSuper = true)
public class TimeCardResource extends ResourceSupport implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(nullsUsing = EmptyStringSerialilzer.class)
    private String workPlaceUuid;

    @Valid
    private List<DailyAttendanceResource> attendances;

    @JsonSerialize(nullsUsing = EmptyStringSerialilzer.class)
    private String note;

    @Null
    private Boolean stored;

    @Null
    private WorkPlace workPlace;

    public TimeCardResource addDailyAttendance(DailyAttendanceResource dailyAttendanceResource) {
        if (attendances == null) {
            this.attendances = new ArrayList<>();
        }
        attendances.add(dailyAttendanceResource);
        return this;
    }

}
