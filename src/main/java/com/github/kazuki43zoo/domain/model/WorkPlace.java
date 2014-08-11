package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkPlace implements Serializable {
    public static final String MAIN_OFFICE_UUID = "00000000-0000-0000-0000-000000000000";
    private static final long serialVersionUID = 1L;
    private String workPlaceUuid;
    private String workPlaceName;
    private LocalTime baseBeginTime;
    private LocalTime baseFinishTime;
    private LocalTime unitTime;

    public boolean isTardyOrEarlyLeaving(LocalTime beginTime, LocalTime finishTime) {
        return (beginTime != null && beginTime.isAfter(baseBeginTime))
                || (finishTime != null && finishTime.isBefore(baseFinishTime));
    }
}
