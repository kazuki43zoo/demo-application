package com.github.kazuki43zoo.domain.repository.timecard;

import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import org.apache.ibatis.annotations.Param;

public interface WorkPlaceRepository {

    WorkPlace findOne(@Param("workPlaceUuid") String workPlaceUuid);

}
