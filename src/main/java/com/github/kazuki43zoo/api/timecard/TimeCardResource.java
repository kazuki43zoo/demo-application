package com.github.kazuki43zoo.api.timecard;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.github.kazuki43zoo.infra.jackson.map.EmptyStringSerializer;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.ToString(callSuper = true)
public class TimeCardResource extends ResourceSupport implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(nullsUsing = EmptyStringSerializer.class)
    private String workPlaceUuid;

    @Valid
    private List<DailyAttendanceResource> attendances;

    @JsonSerialize(nullsUsing = EmptyStringSerializer.class)
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
